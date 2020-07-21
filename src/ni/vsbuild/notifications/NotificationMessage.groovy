package ni.vsbuild.notifications

import ni.vsbuild.PipelineResult

public class NotificationMessage implements Serializable {

   public static String getSimpleMessage(def script, PipelineResult result) {
      def message = "Build ${script.env.JOB_NAME} ${result.toString()}."
      return message
   }

   public static String getDetailedMessage(def script, PipelineResult result) {
      def message = """
         Build ${script.env.BUILD_ID} of ${script.env.JOB_NAME} finished with a result of ${result.name()}.
         ${script.env.BUILD_URL}
         """.stripIndent()
      return message
   }
}
