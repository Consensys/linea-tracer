FROM ubuntu:22.04
ARG VERSION="dev"

RUN apt-get update  && \
    apt-get install --no-install-recommends -q --assume-yes ca-certificates=20* ca-certificates-java=20* && \
    apt-get install --no-install-recommends -q --assume-yes curl=7* wget=1.21* jq=1.6* net-tools=1.60* openjdk-19-jdk-headless=19* libjemalloc-dev=5.* && \
    apt-get clean  && \
    rm -rf /var/lib/apt/lists/*  && \
    adduser --disabled-password --gecos "" --home /opt/sequencer sequencer && \
    chown sequencer:sequencer /opt/sequencer && \
    chmod 0755 /opt/sequencer

USER sequencer
WORKDIR /opt/sequencer

COPY --chown=sequencer:sequencer sequencer /opt/sequencer/

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
