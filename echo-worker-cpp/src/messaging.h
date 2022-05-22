#pragma once
#include <stdlib.h>
#include <rabbitmq-c/amqp.h>
#include <rabbitmq-c/tcp_socket.h>
#include <json.h>

using namespace std;


namespace messaging{

    /*template <typename M>
    class MessageConvertor{
            public:
                virtual M parseMessage(amqp_message_t message);
    };

    class JsonMessageConvertor : public MessageConvertor<Json::Value>{
            public:
                Json::Value parseMessage(amqp_message_t message) override;
    };*/


    
    class Listener {

        private:
            const char * queue = "queue.processing.echo.worker";
            const char * exchange = "exchange.processing.echo";
            const char * routingKey = "processing.echo";
            
            amqp_connection_state_t conn;
            amqp_socket_t *socket;
            amqp_bytes_t queuename;

        public:
            int init(const char * hostname, int port);

            int listen(void callback(amqp_envelope_t envelope, amqp_rpc_reply_t reply));

            //int listenJson(void callback(Json::Value message, amqp_envelope_t envelope, amqp_rpc_reply_t reply));

            /*template <typename M>
            int listen(void callback(M message, amqp_envelope_t envelope, amqp_rpc_reply_t reply), MessageConvertor<M> convertor);*/


            int close();
    };

    class Sender {

        public:
            int init();
            int send();
            int close();
    };


}