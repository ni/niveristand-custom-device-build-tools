import ni.vsbuild.BuildFlow
import ni.vsbuild.BuildInformation

def call(lvVersions) {
   return new BuildInformation('veristand', lvVersions, BuildFlow.NIBUILD)
}
