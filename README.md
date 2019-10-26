# Simple Elastic Beanstalk Application Template
Sample Configuration project for small Spring Boot web applications 
that can be deployed into a single EC2 in an Elastic Beanstalk 
Multicontainer Docker Environment, using two or more app instances 
and an nginx service configured to generate ssl certificates and 
perform load balancing among app instances.

## Steps to setup.

### 1.- Setup your sub-domain in config files.

For this example we use subdomain 'sampledocker' 
in domain 'grayraccoon.com'.

You need to update domain configured in the next files:

__release-https/Dockerrun.aws.json__
```json
        {"name": "URL","value": "grayraccoon.com"},
        {"name": "SUBDOMAINS","value": "sampledocker,"},
```

__release-https/restore-ssl.sh__
and
__release-https/backup-ssl.sh__
```bash
MAIN_DOMAIN_ID="sampledocker.grayraccoon.com"
```

__release-https/proxy-conf/nginx/site-confs/webapp.conf__
```bash
server {
    listen 80;
    return 301 https://sampledocker.grayraccoon.com$request_uri;
}
...
server {
    listen 80;
    server_name sampledocker.grayraccoon.com;
}
...
server {
    listen 443 ssl http2;
    server_name sampledocker.grayraccoon.com;
}
...
```

### 2.- Build your Java-app using ant tasks in maven.

Build app with:
```bash
mvn clean install
```

This sample app contains some ant tasks that once your app jar is ready will:

1. Make a copy of _release-http_ and _release-https_ folders into 
build directory (target/). 
1. Copy generated exec jar from build directory to both folders java-app folder.
1. Create a new Zip for each release-http and release-https. 

These generated zip files are ready be deployed into 
a AWS Elastic Beanstalk Multicontainer Docker Environment.

#### release-http.zip
Use release-http.zip when you don't need the SSL configuration.
This version uses _nginx_ image to load balance the sample app. 

#### release-https.zip
Use release-https.zip when you __need__ the SSL configuration.
This version uses _linuxserver/letsencrypt_ image to generate 
SSL Certificates for your app domain and its embedded nginx 
to load balance the sample app. 

This version also includes some jobs 
that will try to backup and restore ssl configuration. 


### 3.- Create Elastic Beanstalk Multicontainer Docker Environment 

Go to your AWS account and create a simple 
EB Multicontainer Docker Env.

Create the env using sample code.

While creating Environment configure:

__Enable:__  
"Instance log streaming to CloudWatch Logs"

__Set:__  
"Modify rolling updates and deployments" = _Immutable_

__Enable:__
"Monitoring" -> "Ignore HTTP 4xx"


And then wait until environment is created.

### 4.- Enable HTTPS in security group

Access to EC2 Dashboard, go to generated security group, 
and enable HTTPS traffic. 

### 5.- Grant some policies to ec2-role.
Access to IAM,
go to generated ec2-role, and enable the following policies:
* AmazonAPIGatewayPushToCloudWatchLogs
* AmazonS3FullAccess

'AmazonAPIGatewayPushToCloudWatchLogs': 
It is required to publish your app logs to cloud watch.

'AmazonS3FullAccess':
It is required to backup or restore SSL config from a S3 instance.

### 6.- Create S3 where to backup or restore SSL config.
For this sample, we created 'gr-general-config' bucket.

Create your own bucket and __block any public access__.

Update your bucket name in files:
__release-https/restore-ssl.sh__
and
__release-https/backup-ssl.sh__
```bash
S3_BUCKET="gr-general-config"
```

and make sure to re-build app.

### 7.- Configure your sub-domain
Go to your DNS management,
and configure a CNAME record from your sub-domain to generated env url.

For this Sample, we configured: _sampledocker.grayraccoon.com_  
to point to some domain like: _sampleapp-dev-qa.us-east-2.elasticbeanstalk.com_ 

so that, You can access to your app using your sub-domain.

### 8.- Deploy

Take your generated 'release-http' or 'release-https' 
and deploy into your configured environment.

That's it!

If you try to access to your sub-domain 
it will print the port that served the content.

For this example:  
https://sampledocker.grayraccoon.com/

Would print:
```json
{"port":"5000"}
```
or 
```json
{"port":"5100"}
```


__Note:__
_First time this process will take long time, 
 since it will require to create your ssl certificate._

