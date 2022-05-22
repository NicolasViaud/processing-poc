cmake_minimum_required(VERSION 3.9)

project(echo-worker-cpp)

include_directories(${PROJECT_SOURCE_DIR}/include)
add_executable(${PROJECT_NAME} src/app.cpp src/messaging.cpp)

#Rabbitmq client
add_library(lib-rabbitmq-client STATIC IMPORTED)
set_property(TARGET lib-rabbitmq-client PROPERTY IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/tpl/rabbitmq-c/build/librabbitmq/librabbitmq.a)
target_link_libraries(${PROJECT_NAME} lib-rabbitmq-client)
target_include_directories(lib-rabbitmq-client INTERFACE ${PROJECT_SOURCE_DIR}/tpl/rabbitmq-c/include/ ${PROJECT_SOURCE_DIR}/tpl/rabbitmq-c/build/include/)



#Json
add_library(lib-json STATIC IMPORTED)
set_property(TARGET lib-json PROPERTY IMPORTED_LOCATION ${PROJECT_SOURCE_DIR}/tpl/jsoncpp/build/lib/libjsoncpp.a)
target_link_libraries(${PROJECT_NAME} lib-json)
target_include_directories(lib-json INTERFACE ${PROJECT_SOURCE_DIR}/tpl/jsoncpp/include/json/)
