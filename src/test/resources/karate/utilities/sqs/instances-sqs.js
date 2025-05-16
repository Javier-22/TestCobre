// Instanciamos
function SQSManager() {
    var S3Util = Java.type('co.cobre.lib.qa.aws.S3Util');
      var FileUtils = Java.type('co.cobre.lib.qa.util.FileUtils');

      var s3Manager = new S3Util();
      var sqsManager = karate.callSingle('classpath:karate/utilities/sqs/instances-sqs.js.js');

//      // Función para pausar ejecución
//      function waitTime(seconds) {
//        java.lang.Thread.sleep(seconds * 1000);
//      }

      return {
        S3Manager: s3Manager,
        SQSManager: sqsManager,
       /* waitTime: waitTime,*/
//        FileUtils: FileUtils,
//        bucketName: 'test-automation-qa',
//        folderRecaudoFiles: 'files-to-cash-in'
      };
    }