# UPnP Cmd

A simply Gogo shell command for inspecting UPnP devices

## Build
Install Java and Maven 3
```shell
git clone https://github.com/osgistuff/upnp.cmd.git
cd upnp.cmd
mvn clean install
```

## Run
Install the [Apache Felix framework](http://felix.apache.org/downloads.cgi)

```shell
cd ..
cd felix
java -jar bin/felix.jar
```

In the Apache Felix Gogo Shell
```
start http://apache.mirrors.ovh.net/ftp.apache.org/dist//felix/org.osgi.compendium-1.4.0.jar
start http://apache.mirrors.ovh.net/ftp.apache.org/dist//felix/org.apache.felix.log-1.0.1.jar
start http://apache.mirrors.ovh.net/ftp.apache.org/dist//felix/org.apache.felix.upnp.extra-0.4.0.jar
start http://apache.mirrors.ovh.net/ftp.apache.org/dist//felix/org.apache.felix.upnp.basedriver-0.8.0.jar
start http://apache.mirrors.ovh.net/ftp.apache.org/dist//felix/org.apache.felix.upnp.tester-0.4.0.jar

start http://apache.mirrors.ovh.net/ftp.apache.org/dist//felix/org.apache.felix.ipojo-1.12.1.jar
start http://apache.mirrors.ovh.net/ftp.apache.org/dist//felix/org.apache.felix.ipojo.api-1.12.1.jar
start file:///../upnp.cmd/target/upnp.cmd-0.1.0.jar

lb

help

upnp:devices
upnp:subscribe uuid:XXXXXX urn:microsoft.com:serviceId:X_MS_MediaReceiverRegistrar
upnp:unsubscribe uuid:XXXXXX urn:microsoft.com:serviceId:X_MS_MediaReceiverRegistrar
```

## Todolist
Extra commands for:
* [Audio-Video (AV)](http://upnp.org/specs/av/av4/ )
* [Digital Security Camera (DSC)](http://upnp.org/specs/ha/digitalsecuritycamera/)
* [Internet Gateway Device (IGD)](http://upnp.org/specs/gw/igd2/)

## Ressources
* [https://osgi.org/javadoc/r4v42/org/osgi/service/upnp/package-summary.html Javadoc OSGi UPnP Driver]

## License

Copyright (C) 2016 Didier DONSEZ
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.