package ni.vsbuild.steps

import ni.vsbuild.BuildConfiguration

interface Step extends Serializable {

   void execute(BuildConfiguration configuration)

}
