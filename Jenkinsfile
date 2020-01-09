node {

	stage('Initialize'){
	    env.repositoryName = "sl"
        env.deliverableName = "enterprise-portal-java"
        env.containerRegistry = "10.49.145.193:8090"
        
        def dockerHome = tool 'docker'
        env.dockerDaemonURL = "tcp://10.49.145.110:2375"
        env.PATH = "${dockerHome}/bin:${env.PATH}"
    }
    
    stage('Checkout') {    
        checkout scm
    }
    
    stage('Extract maven version from pom') {
      	env.pom = readMavenPom file: 'pom.xml'      	
      	final extVer = env.pom.substring(env.pom.lastIndexOf(':') +1, env.pom.length())  
        env.pomVersion = extVer        
        echo "Maven Version ${env.pomVersion}"
        env.BUILD_ID = env.pomVersion
    } 
    
    stage('Build Java') {
    	withEnv(["JAVA_HOME=${ tool 'JDK 8' }", "PATH+MAVEN=${tool 'M3'}/bin:${env.JAVA_HOME}/bin"]) {		    
		    sh "mvn clean package -Dmaven.test.skip=true"
		
		}
    }   
    /*
    stage('Execute Unit Tests & SonarQube') {
    	withEnv(["JAVA_HOME=${ tool 'JDK 8' }", "PATH+MAVEN=${tool 'M3'}/bin:${env.JAVA_HOME}/bin"]) {		    
		    sh "mvn clean test sonar:sonar -Dsonar.projectKey=SCSDEMO -Dsonar.host.url=http://sonarqube-sonarqube:9000 -Dsonar.login=1ab7d99728fd6cb3c77444a32e3785d147208e2d -Pbuild"		    		
		}
    }    
    
    stage('Build & Publish Documentation') {
    	withEnv(["JAVA_HOME=${ tool 'JDK 8' }", "PATH+MAVEN=${tool 'M3'}/bin:${env.JAVA_HOME}/bin"]) {		    
		    sh "mvn clean verify -Pdocumentation"
		
		} 
    }
    */
    stage('Deploy Java Artifact') {
    	withEnv(["JAVA_HOME=${ tool 'JDK 8' }", "PATH+MAVEN=${tool 'M3'}/bin:${env.JAVA_HOME}/bin"]) {		    
		    sh "mvn deploy helm:package helm:deploy -s mvn-settings.xml -Dmaven.test.skip=true -Pbuild -X -e"
		
		}
    }
    
	stage('Docker Build') {	   
		docker.withServer("${env.dockerDaemonURL}") {
			sh "docker build -t ${env.repositoryName}/${env.deliverableName}:${env.pomVersion} ."        	
		}
	}
 
 	stage('Docker Tag Nightly') {	   
		docker.withServer("${env.dockerDaemonURL}") {
			sh "docker tag ${env.repositoryName}/${env.deliverableName}:${env.pomVersion} ${env.containerRegistry}/${env.repositoryName}/${env.deliverableName}:${env.pomVersion}.${env.BUILD_NUMBER}"
			sh "docker tag ${env.repositoryName}/${env.deliverableName}:${env.pomVersion} ${env.containerRegistry}/${env.repositoryName}/${env.deliverableName}:latest"
		}
	}
	
	stage('Docker Push Nightly') {
      withCredentials([usernamePassword(credentialsId: 'docker-nightly', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
      	docker.withServer("${env.dockerDaemonURL}") {
	        sh "docker login -u ${USERNAME} -p ${PASSWORD} http://${containerRegistry}"
	        sh "docker push ${env.containerRegistry}/${env.repositoryName}/${env.deliverableName}:${env.pomVersion}.${env.BUILD_NUMBER}"
	        sh "docker push ${env.containerRegistry}/${env.repositoryName}/${env.deliverableName}:latest"
        }
      }
	}
}