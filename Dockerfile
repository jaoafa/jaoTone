FROM maven:3 AS builder

WORKDIR /build
COPY pom.xml /build/pom.xml
RUN mvn -B package; echo ""

COPY src /build/src
RUN mvn -B package

FROM amazoncorretto:21.0.7

# hadolint ignore=DL3033
RUN yum install -y python3 python3-pip tar xz && \
    yum clean all

WORKDIR /usr/local/bin

RUN curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/local/bin/yt-dlp && \
    chmod a+rx /usr/local/bin/yt-dlp && \
    curl -LO https://johnvansickle.com/ffmpeg/releases/ffmpeg-release-amd64-static.tar.xz && \
    tar xvf ffmpeg-release-amd64-static.tar.xz && \
    mv ffmpeg-*-amd64-static/ffmpeg . && \
    rm -rf ffmpeg-*-amd64-static ffmpeg-release-amd64-static.tar.xz

WORKDIR /app

COPY --from=builder /build/target/jaoTone-jar-with-dependencies.jar .

ENTRYPOINT []
CMD ["java", "-jar", "jaoTone-jar-with-dependencies.jar"]
