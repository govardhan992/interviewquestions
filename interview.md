## CI/CD process in your project ?

Ans) we have setup devops tools chain where we have setup ci/cd process in place.

For scm all source code repositories maintained by git and github, git is local repository and github is a remote repository.
For CI we are using Jenkins. CI is whenever changes are committed by  developer or any body in github repository controllers automatically trigger into Jenkins pipeline job.

There are many stages in ci/cd process

In the first stage the code is fetched or checkout  code from GitHub, since we are integrated with GitHub and Jenkins,  its automatically fetched code from GitHub then start the building. Here we have a 2 projects. One is java based and another one is nodejs projects. Java based projects I have used maven as a build tool, nodejs project I have used npm as a build.

Once source code fetched into ci the next step we are going to build that after build is successful build artifacts are generated. For NodeJS we are generate  npm packages and for java based projects we are generated jar or war artifacts.

Once the packages are done we are going to sonarqube stage, in this stage scan the source code and gather information about code check qualities and code smells, coverage and bugs exposed into sonar ui.

Once cmplted the SonarQube we are going to build stage, in this stage we are build the images using dockerfile and artifacts.

Once your build the images are sorted and kept into the jfrog artifactory.

Once done all stages  the testers take images from antifactory and test it, once cmplted the testing we deploy the application into eks.


## Kubernetes architecture  ? 

The architecture of k8s consist of  one master node and multiple worker nodes, the master node is brain of the cluster. It is running on control plane.

The master node consists of different components.

APIVERSION:  it is a primary component of  master node and It is central point of communication between all other components of cluster. Including etcd datastore, schedular, control manager and also communicate with kubelet on each worker node to gather the information about the state of the cluster  .

ETCD: is stores the data including information about pod service, and other resources it also store the information about the current state of the cluster. It provides high availability and ensures that the data stores in etcd is always available and consistent .

CONTROL MANAGER:  Its watches the k8's api for changes and make sure that current state of the cluster matches to the desired state.

SCHEDULAR: Its handles the pod creation and management. It is responsible for make sure that pods are running on the most appropriate worker nodes based on the factors such as available resources, constraints and policies.

WORKERNODE:

KUBELET:   kubelet is the "agent" that runs on each worker node. It is responsible for maintaining the state of node and the container running on it. It is communicate with the Kubernetes control plane to report the status of node.

KUBE-PROXY: kube-proxy is assign an Ip address to each pod. Kubeproxy runs on each node and make sure that each pod will get own unique ip address.

Container-engine: container engine also know as the container run time. The software  that is responsible for starting and managing containers on a host.

## What is different between RUN and Command ? 

The RUN Instruction is used to execute a command during the build process, when the image is being created this is typically 
Used to install packages and build applications.

The CMD instruction is used to specify the default command that should be executed when a container run based on the image.

## What is different between CMD and ENTRYPOINT ?

CMD: sets default parameters that can be overridden from docker command cli when container is running.
ENTRYPOINT: sets default parameters cannot be overridden   when docker container runs with cli parameters.


Write a simple Docker file ?

FROM node:14-alpine

WORKDIR  /app

COPY    package*.JSON ./

RUN     npm install

COPY  ..

EXPOSE 3000

CMD  ["npm" "start"]


## Write a shell script for find the error in logfiles ?

#!/bin/bash

Search= "ERROR"
Output file= "file.txt"

Find  "dir path"  type -f "file name" | while read file
do
RESULT = $( grep "$search" "$file")

If [ ! -z $RESULT]
Then
 echo "the error in log files are $RESULT" >> $outputfile
Fi
Done


## Write a simple jenkins file ?


pipeline {
    agent any
     tools {
          maven 'maven 3.9.0' 
    }
    stages {
        stage('check out'){
            steps {
                git 'https://github.com/govardhan992/spring-boot-mongo-docker.git'
                sh 'ls -l'
            }
        }
        stage('build'){
            steps {
                sh "mvn clean package"
            }
        }
         stage('build the docker image'){
            steps {
                sh "docker build -t govardhanr992/spring-boot-mongo ."
            }
        }
        stage("push docker image"){
            steps {
               withCredentials([string(credentialsId: 'docker_hub_pwd', variable: 'docker_hub_pwd')]) {
                
               sh "docker login -u govardhanr992 -p ${docker_hub_pwd}"
     }
                sh "docker push govardhanr992/spring-boot-mongo"
            }
        }
         // Remove local image in Jenkins Server
    stage("Remove Local Image"){
        steps {
        sh "docker rmi -f dockerhandson/spring-boot-mongo"
        }
        }
        stage('kubernates'){
          steps{
         kubeconfig(credentialsId: 'k8s', serverUrl: '') {
            
               sh 'kubectl apply -f springBootMongo.yml'
        }
          }
          }
     }
    }

## What is sticky bit in Linux ?

Sticky bit is a special permission bit that can be used to set up on a file or directory. When the sticky bit is set up on a file or directory it allows only the owner of a file to delete or rename the file within directory

CMD: chmod +t file or directory


## What is Softlink and Hardlink ?

Softline is like a short cut or alias to another file. Its has a small file that points to the original file or directory. If delete the original file or directory . The softlink will be brocken.

CMD: ln -s

Hardlink is like a clone of a file. It is another name of samefile. If you create hardlink to a file and then delete a original file , the hardlink will still work because its points to the same physical data on disk. 

## what is incremental analysis in sonarqube ?

Incremental analysis in SonarQube is a feature that allows the user to analyze only the changed or added code since the last analysis instead of analyzing the entire codebase. This can significantly reduce the time it takes to run an analysis and can be very useful in large projects where a full analysis can take a long time.
When incremental analysis is enabled, SonarQube will only analyze the code that has changed since the last analysis. It does this by comparing the current code with the previous version and identifying the differences. The analysis results are then merged with the previous analysis results to give a complete picture of the code quality.
To use incremental analysis in SonarQube, you need to configure your project to use it. This can be done by setting the "sonar.analysis.mode" property to "incremental" in your SonarQube configuration file. Once this is done, SonarQube will automatically use incremental analysis for all future analyses of that project.
It is important to note that incremental analysis should not be used as a substitute for regular full analyses. Full analyses are still necessary to ensure that the entire codebase is up-to-date and that all issues are being addressed. Incremental analysis should be used as a supplement to full analyses to make the analysis process more efficient.


## how to enable incremental analysis in sonarqube

To enable incremental analysis in SonarQube, you need to follow these steps:

1. Login to your SonarQube instance with an account that has administrative permissions.
2. Navigate to the project for which you want to enable incremental analysis.
3. Click on the "Administration" button in the top menu bar and select "Configuration" from the drop-down menu.
4. In the configuration page, search for the property "sonar.analysis.mode" using the search box provided.
5. Set the value of the "sonar.analysis.mode" property to "incremental" and click on the "Save" button at the bottom of the page.
6. After making this change, the next analysis run will be incremental, analyzing only the changed or added code since the last analysis.
It is important to note that incremental analysis is only available for certain languages and analysis types in SonarQube. Additionally, you should ensure that your SonarQube version supports incremental analysis, as it was introduced in version 5.3.


