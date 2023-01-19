for (int i = 0; i<remainingScratchOrgs ; i++) {     
                properties([
                    parameters([
                        string(name:'sqxBranchName',defaultValue:'DOPS-131', description:'Enter the commit hash or branch number'),
                        string(name:'uiBranchName',defaultValue:'12.0.0', description:'Specify cqForm branch to deploy'),
                        // string(name:'devHubUsername',defaultValue:'example@devhub.cq.com', description:'Enter Devhub username which will be used to create the scratch org')
                    ])
                ])
                //alias the loop variable to refer it in the closure
                def vmNumber = i 
                def ORG_ALIAS = 'CQDevOrg'+String.valueOf(vmNumber)+String.valueOf(BUILD_NUMBER);
                sh "sfdx force:org:create -a ${ORG_ALIAS} -f test/config/project-scratch-def.json -d 3 -v adarshashrestha957@wise-otter-uzvs4t.com"
                sh "sfdx force:user:password:generate -u ${ORG_ALIAS}"
                def datas = sh(script: "sfdx force:user:display -u ${ORG_ALIAS} --json", returnStdout: true)
                def resultpass = readJSON text: datas
                def password = resultpass.result.password
                def username = resultpass.result.username
                 //Store the credential in jenkins global directory
                writeFile file: "$JENKINS_HOME/scratch-org-credentials/${ORG_ALIAS}", text: "${ORG_ALIAS}\n$username\n$password"