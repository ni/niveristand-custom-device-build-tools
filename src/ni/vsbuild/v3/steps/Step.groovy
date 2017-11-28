package ni.vsbuild.v2.steps

import ni.vsbuild.v2.BuildConfiguration

interface Step extends Serializable {

   void execute(BuildConfiguration configuration)

}
