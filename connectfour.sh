#!/bin/sh

cd `dirname $0`
java -splash:banner.png -cp bin application.ConnectFour
