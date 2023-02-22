// imports the required classes for the script to run.
import jenkins.model.*
import hudson.model.*
import hudson.security.*
import jenkins.security.*

def numberOfDays = 30 // sets the variable numberOfDays to 30

def allUsers = Jenkins.instance.getSecurityRealm().getAllUsers()  // retrieves a list of all Jenkins users

  // starts a loop that iterates over each Jenkins user in the allUsers list
  allUsers.each { user ->
  // For each user, the script retrieves the last login time using the getProperty() method of the UserProperty class
  def lastLoginTime = user.getProperty(jenkins.security.LastGrantedAuthoritiesProperty.class)?.lastLogin  
  
  //If the last login time is not null, the script calculates the number of days since the user last logged in.
  if (lastLoginTime != null) {
    def currentTime = System.currentTimeMillis()
    def daysSinceLastLogin = (currentTime - lastLoginTime) / (1000 * 60 * 60 * 24)

    //if the number of days since the user last logged in is greater than the numberOfDays variable.
    //If it is, the script removes the user's access and prints a message to the console.
    if (daysSinceLastLogin > numberOfDays) {
      def userId = user.getId()
      def userToRemove = User.get(userId, false, null)

      if (userToRemove != null) {
        userToRemove.delete()
        println "Revoked access for user: ${userToRemove.fullName}"
      }
    }
  }
}
// creates a list of all Jenkins user names using the collect() method.
def allUserNames = Jenkins.instance.getSecurityRealm().getAllUsers().collect { it.getFullName() }

//prints the list of Jenkins user names to the console
println "All Jenkins users: ${allUserNames}"
