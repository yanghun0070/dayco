#!/bin/sh

#이미지를 빌드하는 명령어는 생성할 이미지 명
docker build --tag dayco:1.0 .

# Docker Image 목록
docker images

# 도커이미지 잘 동작하는 지 컨테이너 확인
docker run -p 8080:8080 -it dayco:1.0 /bin/bash

