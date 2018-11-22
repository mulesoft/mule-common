Map pipelineParams = [
        jdkTool                    : "JDK7",
        enableAllureTestReportStage: false,
        enableSonarQubeStage       : false,
        mavenAdditionalArgs        : "-Dmaven.test.failure.ignore=true",
        mavenDeployGoal            : "install javadoc:jar -DskipTests -DskipITs -Dinvoker.skip=true -Dlicense.skip=true"]

runtimeProjectsBuild(pipelineParams)
