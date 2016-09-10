./gradlew assDeb
adb install -r app/build/outputs/apk/app-debug.apk
adb shell am start -n aabrasha.ua.streettranslator/aabrasha.ua.streettranslator.activity.SearchActivity