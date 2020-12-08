import socket

class Connection(object):
    def __init__(self, pServerIP, pServerPort, encoding='utf8'):
        self.encoding = encoding
        self._socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self._socket.connect((pServerIP, pServerPort))

    def send(self, pMessage):
        if pMessage[-1] != '\n':
            pMessage += '\n'
        self._socket.sendall(bytes(pMessage, self.encoding))

    def receive(self):
        buf = ['']
        while buf[-1] != '\n':
            buf.append(self._socket.recv(1).decode(self.encoding))
        if buf[-2] == '\r':
            return ''.join(buf[:-2])
        else:
            return ''.join(buf[:-1])

    def close(self):
        self._socket.close()
