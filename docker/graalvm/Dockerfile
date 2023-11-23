FROM ghcr.io/graalvm/graalvm-ce:ol9-java17
ARG VERSION="dev"

RUN adduser --home /opt/sequencer sequencer && \
    chown sequencer:sequencer /opt/sequencer && \
    chmod 0755 /opt/sequencer

USER sequencer
WORKDIR /opt/sequencer

COPY --chown=sequencer:sequencer sequencer /opt/sequencer/
RUN chmod -R 755 /opt/sequencer

# Expose services ports
# 8888 HTTP JSON-RPC
EXPOSE 8888

# defaults for host interfaces
ENV SEQUENCER_RPC_HTTP_HOST 0.0.0.0
ENV SEQUENCER_PID_PATH "/tmp/pid"


ENV OTEL_RESOURCE_ATTRIBUTES="service.name=sequencer,service.version=$VERSION"

ENV OLDPATH="${PATH}"
ENV PATH="/opt/sequencer/bin:${OLDPATH}"

ENTRYPOINT ["sequencer"]
HEALTHCHECK --start-period=5s --interval=5s --timeout=1s --retries=10 CMD bash -c "[ -f /tmp/pid ]"

# Build-time metadata as defined at http://label-schema.org
ARG BUILD_DATE
ARG VCS_REF
LABEL org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.name="sequencer" \
      org.label-schema.description="besu transaction sequencer" \
      org.label-schema.vcs-ref=$VCS_REF \
      org.label-schema.vcs-url="https://github.com/Consensys/besu-sequencer-plugins.git" \
      org.label-schema.vendor="Consensys" \
      org.label-schema.version=$VERSION \
      org.label-schema.schema-version="1.0"
