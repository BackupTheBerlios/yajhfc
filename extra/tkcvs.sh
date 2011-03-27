#!/bin/bash

cat $HOME/.ssh/id_rsa.pub | ssh jwolz@shell.berlios.de 'cat > .ssh/authorized_keys'

tkcvs

ssh jwolz@shell.berlios.de 'rm .ssh/authorized_keys'


