echo "Building eventbus"
call mvn clean install -f "BuildingBlocks/EventBus/eventbus"
if %ERRORLEVEL% neq 0 (
   echo "Error in building EventBus"
)

echo "Building eventbus-rabbitmq"
call mvn clean install -f "BuildingBlocks/EventBus/eventbus-rabbitmq"
if %ERRORLEVEL% neq 0 (
   echo "Error in building eventbus-rabbitmq"
)


echo "Building integration-eventlog"
call mvn clean install -f "BuildingBlocks/EventBus/integration-eventlog"
if %ERRORLEVEL% neq 0 (
   echo "Error in building integration-eventlog"
)

echo "Building ordering-domain"
call mvn clean install -f "Services/Ordering/ordering-domain"
if %ERRORLEVEL% neq 0 (
   echo "Error in building ordering-domain"
)

echo "Building ordering-infrastructure"
call mvn clean install -f "Services/Ordering/ordering-infrastructure"
if %ERRORLEVEL% neq 0 (
   echo "Error in building ordering-infrastructure"
)

echo "Building orderapi"
call mvn clean install -f "Services/Ordering/orderapi"
if %ERRORLEVEL% neq 0 (
   echo "Error in building orderapi"
)