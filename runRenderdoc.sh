#!/bin/bash
sh gradlew neoforge:1.21.11:createLaunchScripts
renderdoccmd capture -w --opt-hook-children sh "neoforge/versions/1.21.11/build/moddev/runClient.sh"