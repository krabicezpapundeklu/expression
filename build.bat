rd /S /Q src\main\resources\static

pushd src\main\web
call npm install
call npm run build
xcopy build ..\resources\static /E /I
popd

call mvn install -D skipTests
call java -jar target\expression-0.0.1-SNAPSHOT.jar
