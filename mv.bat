@echo off

if exist Z:\ (
    copy /y "monkeBoy-1.0-SNAPSHOT.jar" "Z:\servers\chartersurvival\plugins"
    echo Complete
) else (
    echo Network Drive not connected
    )