package ni.vsbuild.v3.steps

import ni.vsbuild.v3.BuildConfiguration

interface Step extends Serializable {

   void execute(BuildConfiguration configuration)

}
