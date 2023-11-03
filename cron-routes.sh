#!/bin/bash
count=$(lsof -i:80 | wc -c)
if [ $count -le 0 ]
then
  java -jar $HOME/2022-tpa-vi-no-grupo-09/target/ejercicio-1.0-SNAPSHOT.jar
fi