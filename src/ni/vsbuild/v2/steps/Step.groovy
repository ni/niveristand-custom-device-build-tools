package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

interface Step implmements Serializable {

   execute(BuildConfiguration configuration)

}
