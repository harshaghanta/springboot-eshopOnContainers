# Use the official Filebeat image as the base image
FROM docker.elastic.co/beats/filebeat:8.13.3

# Copy the Filebeat configuration file from the host machine into the container
COPY ./EFK/filebeat.yml /usr/share/filebeat/filebeat.yml

# Change the ownership of the configuration file to ensure that Filebeat can read it
USER root
RUN chown root:filebeat /usr/share/filebeat/filebeat.yml
RUN chmod go-w /usr/share/filebeat/filebeat.yml


USER filebeat