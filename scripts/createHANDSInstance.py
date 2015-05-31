import shutil, errno, os, subprocess, datetime, time

ts = time.time()

dropbox_directory = "/Volumes/Martin/Dropbox/workspace/SearchGames"
mounted_drive_directory = "/Volumes/Martin/Dropbox/workspace/SearchGames"

print "Create new HANDS instance"
var = raw_input("Use (1) Local dropbox (2) Mounted drive for copy ");

print var;

if ( var == "1" ):
    root_copy_directory = dropbox_directory;
else: 
    root_copy_directory = mounted_drive_directory
    



jar_directory = root_copy_directory + "/bin"
lib_directory = root_copy_directory + "/lib"
schedule_file = root_copy_directory + "/output/simulationSchedule.txt"

target_directory = "/Users/Martin/Desktop/" + datetime.datetime.fromtimestamp(ts).strftime('%Y%m%d_%H%M%S')

print "Creating files at " + target_directory + "..."

if not os.path.exists(target_directory):
    os.makedirs(target_directory)
    os.makedirs(target_directory + "/output")
    os.makedirs(target_directory + "/output/data")
    os.makedirs(target_directory + "/output/dataArchive")
    os.makedirs(target_directory + "/output/data/charts")
    file = open(target_directory + "/output/simRecordID.txt",'w')
    
def copyanything(src, dst):
    try:
        shutil.copytree(src, dst)
    except OSError as exc: # python >2.5
        if exc.errno == errno.ENOTDIR:
            shutil.copy(src, dst)
        else: raise
  
print "Copying bin from " + jar_directory + "..."
      
copyanything(jar_directory, target_directory + "/bin")

print "Copying lib from " + lib_directory + "... (takes ages)"

copyanything(lib_directory, target_directory + "/lib")

print "Copying schedule from " + schedule_file + "..."

copyanything(schedule_file, target_directory + "/output")

os.chdir(target_directory)

print "Running HANDS..."
subprocess.call(['/Library/Java/JavaVirtualMachines/jdk1.8.0_25.jdk/Contents/Home/bin/java', '-XX:-UseGCOverheadLimit', '-Xmx5g', '-Dfile.encoding=US-ASCII', '-classpath', target_directory + '/bin:/Volumes/Martin/Dropbox/workspace/SearchGames/lib/epsgraphics.jar:/Volumes/Martin/Dropbox/workspace/SearchGames/lib/jcommon-1.0.21.jar:/Volumes/Martin/Dropbox/workspace/SearchGames/lib/jfreechart-1.0.17.jar:/Volumes/Martin/Dropbox/workspace/SearchGames/lib/jgrapht-core-0.9.0.jar:/Volumes/Martin/Dropbox/workspace/SearchGames/lib/jgraph-sna.jar:/Volumes/Martin/Dropbox/workspace/SearchGames/lib/java-plot.jar:/Volumes/Martin/Dropbox/workspace/SearchGames/lib/commons-math3-3.4.1.jar', 'Utility.Runner'])
