@rem
@rem Copyright 2011 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_HOME=%DIRNAME%
set APP_NAME="Gradle"
set APP_BASE_NAME=%~n0

@rem Resolve links (%~dp0 in cmd.exe resolves all symlinks to their target)
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fsi

@rem Determine the Java command to use to start the JVM.
set JAVA_CMD=%JAVA_HOME%\bin\java.exe
if not exist "%JAVA_CMD%" set JAVA_CMD=java.exe

@rem Check if JAVA_HOME is set and points to a valid directory
if not "%JAVA_HOME%"=="" (
    if not exist "%JAVA_HOME%\bin\java.exe" (
        echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
        echo.
        echo Please set the JAVA_HOME environment variable in your environment to match the
        echo location of your Java installation.
        goto fail
    )
) else (
    @rem If JAVA_HOME is not set, try to find java.exe in the PATH
    where /q java.exe
    if %errorlevel% neq 0 (
        echo ERROR: JAVA_HOME is not set and java.exe could not be found in your PATH.
        echo.
        echo Please set the JAVA_HOME environment variable in your environment to match the
        echo location of your Java installation, or add the Java bin directory to your PATH.
        goto fail
    )
)

@rem Get the path to the wrapper JAR.
set GRADLE_JAR=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

@rem Check if wrapper JAR exists.
if not exist "%GRADLE_JAR%" (
    echo ERROR: Could not find gradle-wrapper.jar in %GRADLE_JAR%.
    echo Please ensure the wrapper JAR is present or use the 'gradle wrapper' task to regenerate.
    goto fail
)

@rem Load properties from gradle-wrapper.properties
set GRADLE_WRAPPER_PROPERTIES_FILE=%APP_HOME%\gradle\wrapper\gradle-wrapper.properties
if exist "%GRADLE_WRAPPER_PROPERTIES_FILE%" (
    for /f "tokens=1* delims==" %%a in ('type "%GRADLE_WRAPPER_PROPERTIES_FILE%" ^| findstr /b "distributionUrl="') do (
        set WRAPPER_DISTRIBUTION_URL=%%b
    )
    if not "%WRAPPER_DISTRIBUTION_URL%"=="" (
        set GRADLE_DISTRIBUTION_URL_PARAM=-Dgradle.wrapper.distribution="%WRAPPER_DISTRIBUTION_URL%"
    ) else (
        set GRADLE_DISTRIBUTION_URL_PARAM=
    )
) else (
    set GRADLE_DISTRIBUTION_URL_PARAM=
)

@rem Set GRADLE_USER_HOME
set GRADLE_USER_HOME_OPT=
if not "%GRADLE_USER_HOME%"=="" (
    set GRADLE_USER_HOME_OPT=-Dgradle.user.home="%GRADLE_USER_HOME%"
)

@rem Execute Gradle Wrapper
"%JAVA_CMD%" %DEFAULT_JVM_OPTS% %GRADLE_OPTS% %JAVA_OPTS% %GRADLE_USER_HOME_OPT% %GRADLE_DISTRIBUTION_URL_PARAM% -classpath "%GRADLE_JAR%" org.gradle.wrapper.GradleWrapperMain %*

:fail
if "%OS%"=="Windows_NT" endlocal
exit /b %errorlevel%
