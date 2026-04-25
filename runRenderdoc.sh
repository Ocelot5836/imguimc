#!/bin/bash
sh gradlew neoforge:1.21.1:createLaunchScripts
renderdoccmd capture -w --opt-hook-children sh "neoforge/versions/1.21.1/build/moddev/runClient.sh"