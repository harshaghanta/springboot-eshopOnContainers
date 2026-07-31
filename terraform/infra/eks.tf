data "aws_kms_alias" "eshop_kms_alias" {
  name = var.kms_alias_name
}

module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "~> 20.0"

  cluster_name    = var.cluster_name
  cluster_version = var.cluster_version


  create_kms_key      = false
  cluster_encryption_config = {
    provider_key_arn = data.aws_kms_alias.eshop_kms_alias.target_key_arn
    resources        = ["secrets"]
  }  

  # Force EKS (and its node groups/addons) to depend directly on the entire VPC module.
  # This guarantees the NAT Gateways & IGW stay alive until EKS is completely torn down.
  depends_on = [
    module.vpc
  ]

  cluster_addons = {
    coredns = {
      most_recent                 = true
      resolve_conflicts_on_create = "OVERWRITE"
      resolve_conflicts_on_update = "OVERWRITE"
    }

    kube-proxy = {
      most_recent                 = true
      resolve_conflicts_on_create = "OVERWRITE"
      resolve_conflicts_on_update = "OVERWRITE"
    }

    vpc-cni = {
      most_recent                 = true
      resolve_conflicts_on_create = "OVERWRITE"
      resolve_conflicts_on_update = "OVERWRITE"
    }

    metrics-server = {
      most_recent                 = true
      resolve_conflicts_on_create = "OVERWRITE"
      resolve_conflicts_on_update = "OVERWRITE"

      configuration_values = jsonencode({
        containerPort = 10251
      })
    }

    aws-ebs-csi-driver = {
      most_recent                 = true
      resolve_conflicts_on_create = "OVERWRITE"
      resolve_conflicts_on_update = "OVERWRITE"
      service_account_role_arn = module.ebs_csi_irsa_role.iam_role_arn

      configuration_values = jsonencode({
        defaultStorageClass = {
          enabled = true
        }
      })
    }
  }
  

  vpc_id                         = module.vpc.vpc_id
  subnet_ids                     = module.vpc.private_subnets
  cluster_endpoint_public_access = true

  enable_cluster_creator_admin_permissions = true

  # Enable IAM Roles for Service Accounts (IRSA)
  enable_irsa = true

  eks_managed_node_group_defaults = {
    iam_role_additional_policies = {
      AmazonSSMManagedInstanceCore = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
    }
  }

  # Workload separation into dedicated node groups
  eks_managed_node_groups = {
    # PLATFORM NODE GROUP (ArgoCD, Vault, Redis, Istio Ingress)
    platform = {
      name           = "${var.environment}-platform-node-group"
      instance_types = ["m6i.large"]
      capacity_type  = "ON_DEMAND"

      iam_role_use_name_prefix = false
      iam_role_name            = "${var.environment}-platform-node-group-role"

      desired_size = 2
      min_size     = 1
      max_size     = 2

      labels = {
        "category" = "platform"
      }
    }
    # PRODUCT NODE GROUP (Catalog-API, Basket-API, Ordering-API, etc..)
    product = {
      name           = "${var.environment}-product-node-group"
      instance_types = ["m7g.large"]
      capacity_type  = "ON_DEMAND"

      iam_role_use_name_prefix = false
      iam_role_name            = "${var.environment}-product-node-group-role"

      ami_type = "AL2023_ARM_64_STANDARD"

      desired_size = 2
      min_size     = 1
      max_size     = 2

      labels = {
        "category" = "product"
      }
    }

  }

  node_security_group_additional_rules = {
    ingress_istio_webhook = {
      description                   = "Allow EKS Control Plane to communicate with Istio Webhook on port 15017"
      protocol                      = "tcp"
      from_port                     = 15017
      to_port                       = 15017
      type                          = "ingress"
      source_cluster_security_group = true
    }

    ingress_metrics_server = {
      description                   = "Allow EKS Control Plane to communicate with Metrics Server extension API on port 10251"
      protocol                      = "tcp"
      from_port                     = 10251
      to_port                       = 10251
      type                          = "ingress"
      source_cluster_security_group = true
    }
  }
}

# IAM Role required for EBS CSI Driver
module "ebs_csi_irsa_role" {
  source  = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"
  version = "~> 5.0"

  role_name             = "${var.cluster_name}-ebs-csi-role"
  attach_ebs_csi_policy = true

  oidc_providers = {
    main = {
      provider_arn               = module.eks.oidc_provider_arn      
      namespace_service_accounts       = ["kube-system:ebs-csi-controller-sa"]
    }
  }
}