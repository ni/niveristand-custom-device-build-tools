package ni.vsbuild.notifications

import ni.vsbuild.PipelineResult

interface Notification extends Serializable {

   void notify(PipelineResult result)

}
