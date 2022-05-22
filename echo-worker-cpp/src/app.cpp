#include <iostream>
#include <stdlib.h>
#include <rabbitmq-c/amqp.h>
#include <rabbitmq-c/tcp_socket.h>
#include <json.h>
#include "messaging.h"

using namespace messaging;


void onMessage(amqp_envelope_t envelope, amqp_rpc_reply_t reply){
  Json::Reader reader;
  Json::Value message;

  char * body = (char *) envelope.message.body.bytes;
  reader.parse(body, body+envelope.message.body.len, message);

  std::cout << message << std::endl;
  std::cout << message.get("ds", 8) << std::endl;
}

int main()
{

  Listener workerlistener;

  workerlistener.init("localhost", 5672);
  workerlistener.listen(onMessage);
  workerlistener.close();
  return 0;
}


