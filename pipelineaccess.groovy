import jenkins.model.*
import hudson.security.*

def instance = Jenkins.getInstance()
def authStrategy = new hudson.security.GlobalMatrixAuthorizationStrategy()

// Define users
def userA = 'userA'
def userB = 'userB'

// Define pipeline jobs
def pipelineJob1 = 'pipelineJob1'
def pipelineJob2 = 'pipelineJob2'

// Define permissions
def read = hudson.model.Item.READ
def build = hudson.model.Item.BUILD

// Set up access control
authStrategy.add(Jenkins.ADMINISTER, userA) // User A can administer Jenkins
authStrategy.add(read, userA, pipelineJob1) // User A can read pipeline job 1
authStrategy.add(read, userB, pipelineJob1) // User B can read pipeline job 1
authStrategy.add(read, userB, pipelineJob2) // User B can read pipeline job 2
authStrategy.add(build, userB, pipelineJob1) // User B can build pipeline job 1
authStrategy.add(build, userB, pipelineJob2) // User B can build pipeline job 2

instance.setAuthorizationStrategy(authStrategy)
instance.save()
