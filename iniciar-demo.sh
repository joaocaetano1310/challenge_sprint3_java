#!/usr/bin/env sh
cd "$(dirname "$0")" || exit 1
exec java -Dfile.encoding=UTF-8 -jar executavel/futurevet.jar --spring.profiles.active=demo
