#include <iostream>
#include <stdlib.h>
#include <rabbitmq-c/amqp.h>
#include <rabbitmq-c/tcp_socket.h>
#include "app.h"

std::string demo::Greeter::greeting()
{
  return std::string("Hello, World!");
}

int main()
{

  amqp_connection_state_t conn = amqp_new_connection();

  amqp_socket_t *socket = amqp_tcp_socket_new(conn);

  if (!socket)
  {
    std::cout << "Error when creating TCP socket" << std::endl;
  }

  int status = amqp_socket_open(socket, "localhost", 5672);
  if (status)
  {
    std::cout << "Error when opening TCP socket" << std::endl;
  }

  amqp_rpc_reply_t login = amqp_login(conn, "/", 0, 131072, 0, AMQP_SASL_METHOD_PLAIN, "guest", "guest");
  //      if (login) {
  //            std::cout << "Error when logging in" << std::endl;
  //        }

  amqp_channel_open_ok_t *channel = amqp_channel_open(conn, 1);
  //      if (channel) {
  //            std::cout << "Error when opening channel" << std::endl;
  //        }

  amqp_rpc_reply_t rpc = amqp_get_rpc_reply(conn);
  //      if (rpc) {
  //            std::cout << "Error when RPC reply" << std::endl;
  //        }

  amqp_queue_declare_ok_t *r = amqp_queue_declare(
      conn, 1, amqp_empty_bytes, 0, 0, 0, 1, amqp_empty_table);
  amqp_bytes_t queuename = amqp_bytes_malloc_dup(r->queue);
  if (queuename.bytes == NULL)
  {
    std::cout << "Out of memory while copying queue name" << std::endl;
    return 1;
  }

  amqp_queue_bind(conn, 1, queuename, amqp_cstring_bytes("exchange.processing.echo"),
                  amqp_cstring_bytes("processing.echo"), amqp_empty_table);

  amqp_basic_consume(conn, 1, queuename, amqp_empty_bytes, 0, 1, 0,
                     amqp_empty_table);

  for (;;)
  {
    amqp_rpc_reply_t res;
    amqp_envelope_t envelope;

    amqp_maybe_release_buffers(conn);

    res = amqp_consume_message(conn, &envelope, NULL, 0);

    if (AMQP_RESPONSE_NORMAL != res.reply_type)
    {
      break;
    }

    printf("Delivery %u, exchange %.*s routingkey %.*s\n",
           (unsigned)envelope.delivery_tag, (int)envelope.exchange.len,
           (char *)envelope.exchange.bytes, (int)envelope.routing_key.len,
           (char *)envelope.routing_key.bytes);

    if (envelope.message.properties._flags & AMQP_BASIC_CONTENT_TYPE_FLAG)
    {
      printf("Content-type: %.*s\n",
             (int)envelope.message.properties.content_type.len,
             (char *)envelope.message.properties.content_type.bytes);
    }
    printf("----\n");

    //      amqp_dump(envelope.message.body.bytes, envelope.message.body.len);

    amqp_destroy_envelope(&envelope);
  }

  amqp_bytes_free(queuename);

  return 0;
}