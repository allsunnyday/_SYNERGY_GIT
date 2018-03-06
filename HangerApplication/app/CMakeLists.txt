# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)


set(pathOPENCV C:/Users/JHS/Downloads/OpenCV-android-sdk_320)
set(pathPROJECT C:/Users/JHS/Documents/GitHub/HangerApplication)
set(pathLIBOPENCV_JAVA ${pathPROJECT}/app/src/main/JniLibs/${ANDROID_ABI}/libopencv_java3.so)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

include_directories(${pathOPENCV}/sdk/native/jni/include)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library(
             native-lib

             SHARED

             src/main/cpp/native-lib.cpp )

find_library(
              log-lib

              log )

add_library( lib_opencv SHARED IMPORTED )

set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${pathLIBOPENCV_JAVA})



target_link_libraries( # Specifies the target library.
                       native-lib

                       lib_opencv

                       ${log-lib} )