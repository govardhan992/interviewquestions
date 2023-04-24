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

APIServer:  it is a primary component of  master node and It is central point of communication between all other components of cluster. Including etcd datastore, schedular, control manager and also communicate with kubelet on each worker node to gather the information about the state of the cluster  .

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


## Write a simple Docker file ?

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

## What is git merge and rebase?

git merge and git rebase are both Git commands used to integrate changes from one branch into another branch.
git merge is used to combine changes from one branch into another branch. When you run git merge, it creates a new merge commit in the target branch, which links the target branch with the source branch. This merge commit records the state of both branches at the time of the merge and serves as a permanent record of the integration.

git rebase, on the other hand, is a different way of integrating changes from one branch into another. Instead of creating a new merge commit, git rebase reapplies the changes from the source branch onto the target branch, one by one, based on the current state of the target branch. This results in a linear history of changes, without any merge commits.

The choice between git merge and git rebase depends on your personal preference and the project you're working on. If you prefer a clear history of merges and want to preserve the order of commits, git merge is a good choice. If you prefer a more linear history and don't mind re-writing the history of your branch, git rebase might be the better choice. However, git rebase can be more complex and dangerous, as it can result in conflicts and lost work if it's not used carefully.


## how to images are stored in the jfrog artifactory ?

Two steps to do the images are stored in to jfrong artifacory

Step1:  Integrate Artifactory with Jenkins
Step2: Written jenkins pipeline script.

Integrate Artifactory with Jenkins:

Login to Jenkins to integrate Artifactory with Jenkins
	1. Install "Artifactory" plug-in
	• Manage Jenkins -> Jenkins Plugins -> available -> artifactory
	2. Configure Artifactory server credentials
	• Manage Jenkins -> Configure System -> Artifactory
		○ Artifactory Servers
			§ Server ID : Artifactory-Server
			§ URL : Artifactory Server URL
			§ Username :
			§ Password : 
Pipeline Script:

Pipeline {
	Agent any
		Stages{
			Stage("push docker image to artifactory"){
				Steps{
					Withcredentials([usernamepassword(credentials:'artifactory_cred',passwordVariable:' ',usernameVariable:'username')]) {
					  sh "docker push username/imagename:tag"
					}
					}
						}
							}
					}


## how to increase node space in docker file

To increase the node space in a Dockerfile, you can use the '--storage-opt' flag when creating or running the docker 
Container.

## how to add nodes in Jenkins

To add a node in Jenkins, follow these steps:

1. Log in to Jenkins and click on the "Manage Jenkins" link from the left-hand menu.
2. Select the "Manage Nodes and Clouds" option.
3. Click on the "New Node" button on the top right corner of the screen.
4. Enter a name for your new node and select the type of node you want to create (e.g. "Permanent Agent" or "Docker Agent").
5. Fill in the details for the node, such as the number of executors, the remote root directory, and the labels that you want to associate with the node.
6. If you are creating a "Permanent Agent", you will need to specify the hostname or IP address of the node, as well as the credentials to connect to it. If you are creating a "Docker Agent", you will need to specify the Docker image that you want to use for the agent.
7. Click on the "Save" button to create the new node.
Once you have added the new node, you can use it to run Jenkins jobs by specifying the appropriate labels in the job configuration. You can also configure the node to run specific types of jobs or to only be available during certain times of the day.


## How to reset Jenkins admin password.

To reset the Jenkins admin password, follow these steps:
1. Log in to the server where Jenkins is installed.
2. Stop the Jenkins service if it's running. You can do this by running the command sudo systemctl stop jenkins on a Linux server.
3. Navigate to the Jenkins home directory on the server. This is typically located at /var/lib/jenkins.
4. Locate the config.xml file in the Jenkins home directory and open it with a text editor.
5. Search for the <useSecurity>true</useSecurity> tag and change the value to <useSecurity>false</useSecurity>. This will disable security on the Jenkins server temporarily.
6. Save the config.xml file and close the text editor.

## Reset Jenkins Admin’s Password
After Jenkins restarts navigate to the web console. Notice that you were not prompted for a username or password. This is because we disabled security in Jenkins’ configuration file.
If this is a publicly shared Jenkins instance, you should disable public access to the server until the password reset is complete.
To reset admin’s password, do the following.
7. Click on People on the left-hand navigation menu.
8. Click on the Admin.
9. Delete the user account.
10. Navigate to Jenkins / Manage Jenkins.
11. Click on Configure Global Security
12. Check the Enable Security check box
13. Under Security Realm, select Jenkins’ own user database
14. In the Authorization section, select Logged-in users can do anything.
15. Unselect Allow anonymous read access.
16. Click Save to save your changes
17. Once you have completed the tasks above, you will be redirect to a page where a new Admin user can be created. Fill in your new details and then click Create First Admin User.
18. You have no created a new Admin user with a new password.

## How to backup completely Jenkins master job without using plugin.

To backup a Jenkins master job without using a plugin, follow these steps:
1. Log in to the server where Jenkins is installed.
2. Navigate to the Jenkins home directory on the server. This is typically located at /var/lib/jenkins.
3. Copy the entire job directory that you want to back up. The job directories are located in the jobs subdirectory of the Jenkins home directory. The name of the job directory corresponds to the name of the job in Jenkins.
4. Paste the copied job directory to a safe location outside of the Jenkins home directory. This will create a backup of the job that you can restore later if needed.
5. If your Jenkins instance has custom plugins or configurations, you may want to also back up the Jenkins home directory itself.

To restore a backed up job, follow these steps:

1. Log in to the server where Jenkins is installed.
2. Navigate to the Jenkins home directory on the server.
3. Copy the backed up job directory that you want to restore.
4. Paste the copied job directory into the jobs subdirectory of the Jenkins home directory. Make sure to give it the same name as the original job directory.
5. Restart the Jenkins service. You can do this by running the command sudo systemctl restart jenkins on a Linux server.
6. The restored job should now appear in the Jenkins user interface and be available for use.


## Jenkins user account creation.

Jenkins user account creation, following steps:

1. Log in to your Jenkins instance as an administrator.
2. Click on "Manage Jenkins" in the left-hand menu.
3. Click on "Manage Users" in the list of options that appears.
4. Click on the "Create User" button.
5. Enter the user details, including the username, password, full name, and email address.
6. Choose the appropriate "Security Realm" and "Authorization" options for your setup. For example, you may choose to use Jenkins' built-in user database or integrate with an external authentication system.
7. Click the "Create User" button to save the new user.


## Different type of  jobs in Jenkins

Jenkins supports various types of jobs, which can be used to automate different types of software development and deployment tasks. Here are some of the most common types of jobs in Jenkins:

1. Freestyle project: This is a general-purpose job that can execute any kind of shell script, batch file, or command. It can be used to build, test, or deploy software, as well as to perform other automated tasks.

2. Pipeline project: This is a job type that defines a continuous delivery pipeline as a set of stages, each of which can run one or more steps. It provides a way to model complex workflows that involve multiple stages of testing, building, and deployment.

3. Multi-configuration project: This is a job type that can build the same source code on multiple platforms or with different configurations. It can be used to build and test software across different operating systems, architectures, or other variables.

4. Maven project: This is a job type that uses the Maven build system to build, test, and deploy Java applications. It provides integration with Maven repositories and can automatically manage dependencies.

5. Freestyle matrix project: This is a job type that combines the features of the Freestyle and Multi-configuration projects. It allows you to define a matrix of axes that can be used to build and test software across multiple configurations.

6. GitHub organization project: This is a job type that scans a GitHub organization or a GitHub user account for repositories and automatically creates jobs for each repository. It allows you to automatically build and test software hosted on GitHub.


## Cron Jobs and Hooks in Jenkins

Cron Jobs and Hooks are two different mechanisms in Jenkins that are used to schedule and trigger jobs

1. Cron Jobs: Cron is a Unix utility that allows you to schedule commands to run at specific intervals. In Jenkins, you can use Cron syntax to schedule jobs to run at specific times or intervals. To schedule a job using Cron, go to the job's configuration page, and in the "Build Triggers" section, select the "Build periodically" checkbox. You can then enter a Cron expression to specify when the job should run.

2. Hooks: Hooks are a way to trigger a Jenkins job automatically when a specific event occurs. There are several types of hooks supported by Jenkins, including Git hooks, Subversion hooks, and GitHub webhooks. Hooks are typically used to trigger jobs when changes are made to source code, such as a new commit or a merge to a branch. To set up a hook, you need to configure the source code repository to send a notification to Jenkins when a relevant event occurs. You can then configure the Jenkins job to listen for the event and trigger the job when the event occurs.

Both Cron Jobs and Hooks are powerful mechanisms that can be used to automate tasks in Jenkins. Cron Jobs are ideal for scheduling tasks that need to run on a specific schedule, such as nightly builds or periodic backups. Hooks are ideal for triggering tasks when changes are made to source code, such as running tests or deploying the latest version of the application.



import hudson.model.User
import jenkins.model.Jenkins
import org.joda.time.DateTime

def inactiveThreshold = 30
def jenkins = Jenkins.getInstance()
def now = new DateTime()

jenkins.getAllItems(User.class).each { user ->
    def lastLogin = new DateTime(user.getProperty(jenkins.securityRealm).loadUserByUsername(user.getId()).getLastLogin())
    def inactiveDays = (now.minus(lastLogin.getMillis()).getMillis() / 86400000).toInteger()
    if (inactiveDays > inactiveThreshold) {
        jenkins.securityRealm.createCliAuthenticator().authenticate(user.getId(), null)
        def userToDelete = jenkins.getUser(user.getId())
        userToDelete.delete()
        println "Revoked access for user ${user.getId()} who has been inactive for ${inactiveDays} days."
    }
}



## what is Git Sparse 

Git Sparse is a feature in Git that allows you to configure your working directory to include only a subset of files and directories from your repository. This can be useful when working with large repositories that contain many files or when you only need to work with a specific subset of files.
With Git Sparse, you can define a set of patterns that determine which files and directories are included in your working directory. This is done using the git sparse-checkout command, which allows you to specify the patterns that should be included or excluded.
Once you have set up Git Sparse, your working directory will only include the files and directories that match your specified patterns. This can make your repository smaller and faster to work with, as you don't need to download and manage all of the files in the repository.

## docker image pull policy 

In Docker, the "image pull policy" determines when Docker should check for new versions of an image and whether to use a cached version of the image or always pull the latest version.

There are three main image pull policies in Docker:

always: This policy always pulls the latest version of the image, even if a cached version is already present on the host. This can be useful for ensuring that you are always using the most up-to-date version of an image, but it can also be slow if the image is large and changes frequently.

if-not-present: This policy first checks if a cached version of the image is present on the host. If a cached version is found, Docker uses it. If a cached version is not found, Docker pulls the latest version of the image. This can be a good balance between always pulling the latest version and avoiding unnecessary downloads.

never: This policy only uses a cached version of the image and never pulls the latest version, even if a newer version is available. This can be useful for avoiding unnecessary downloads and ensuring that you always use a specific version of an image, but it can also lead to using outdated images.

You can specify the image pull policy when running a Docker command by using the --pull option, followed by one of the above policies. For example:

docker run --pull always my-image
This command will always pull the latest version of the my-image image, even if a cached version is already present on the host.

## How to remove remote branch using git command

git push origin --delete feature/new-feature

## Docker volume types?

Docker provides different types of volumes to store data outside of containers. Some of the commonly used volume types are:

Named Volumes: These are user-defined volumes created and named using the docker volume create command. Named volumes are managed by Docker and can be shared among multiple containers. They provide an easy way to manage data and ensure data persist even when the container is removed.

Host Volumes: Host volumes allow you to mount a directory from the host machine into a container. This means that the data is stored on the host machine and can be accessed by the container. Host volumes are useful for sharing files between the host and the container and for accessing data that is not part of the container image.

Tmpfs Volumes: Tmpfs volumes are created in memory and are useful for storing temporary data that does not need to persist between container runs. Tmpfs volumes are faster than disk-based volumes but are limited by the amount of memory available on the host machine.

Bind Mounts: Bind mounts allow you to mount a file or directory from the host machine into a container, similar to host volumes. However, bind mounts are not managed by Docker and are controlled by the host machine's file system permissions. Bind mounts are useful for quickly sharing files between the host and container during development.

Each volume type has its own use case, and choosing the right type depends on your application's requirements. By using volumes, you can ensure that your data is persistent and can be easily shared between containers, making it a critical feature of Docker.
