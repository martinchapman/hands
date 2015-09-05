### TO DO: Use paramiko to do all Java commands from this script directly, by introducing an interactive paramiko session (example on GitHub). To do this, changes will need to be made to hands to avoid use of GUI as X11 over paramiko is (seemingly) too obscure

import shutil, errno, os, subprocess, datetime, time, sys, zipfile, paramiko, commands, select
import Xlib.support.connect as xlib_connect

IP = '52.16.137.138'

nocopy = False
nokill = False
nodelete = False

if ( len(sys.argv) > 0 ):
    for arg in sys.argv:
        if ( arg == "-ncp"):
            nocopy = True
            nodelete = True
        if ( arg == "-nk"):
            nokill = True
        
ts = time.time()

dropbox_directory = "/Users/Martin/Dropbox/workspace/SearchGames"
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
sample_file = root_copy_directory + "/output/data-sample/20150823_214807.csv"

local_prefix = "/Users/Martin/Desktop/"
target_name = datetime.datetime.fromtimestamp(ts).strftime('%Y%m%d_%H%M%S')
target_directory = local_prefix + target_name

print "Creating files at " + target_directory + "..."

if not os.path.exists(target_directory):
    os.makedirs(target_directory)
    os.makedirs(target_directory + "/output")
    os.makedirs(target_directory + "/output/data")
    os.makedirs(target_directory + "/output/dataArchive")
    os.makedirs(target_directory + "/output/charts")
    os.makedirs(target_directory + "/output/charts/tables")
    file = open(target_directory + "/output/simRecordID.txt",'w')
    
def copyanything(src, dst):
    try:
        shutil.copytree(src, dst)
    except OSError as exc: # python >2.5
        if exc.errno == errno.ENOTDIR:
            shutil.copy(src, dst)
        else: raise

def copymultiple(src, dst):
    src_files = os.listdir(src)
    for file_name in src_files:
        full_file_name = os.path.join(src, file_name)
        if (os.path.isfile(full_file_name)):
            shutil.copy(full_file_name, dst)
  
print "Copying bin from " + jar_directory + "..."
      
copyanything(jar_directory, target_directory + "/bin")

print "Copying lib from " + lib_directory + "... (takes ages)"

copyanything(lib_directory, target_directory + "/lib")

print "Copying schedule from " + schedule_file + "..."

copyanything(schedule_file, target_directory + "/output")

print "Copying sample file from " + sample_file + "..."

copyanything(sample_file, target_directory + "/output/data")

print "Making figures file..."

x = open(target_directory + "/output/charts/figures.bib", 'a').close()

######

def zipdir(path, ziph):
    # ziph is zipfile handle
    for root, dirs, files in os.walk(path):
        for file in files:
            ziph.write(os.path.join(root, file))
            
zipf = zipfile.ZipFile(target_directory + ".zip", 'w')
os.chdir(local_prefix)
zipdir(target_name, zipf)
zipf.close()

shutil.rmtree(target_directory)

#######

remote_prefix = "/home/ec2-user/"

remote_directory = remote_prefix + target_name
    
ssh = paramiko.SSHClient()
ssh.load_system_host_keys()
private_key = paramiko.RSAKey.from_private_key_file("/Users/Martin/Downloads/AWS4815.pem")
ssh.connect(IP, username='ec2-user', password='', pkey=private_key)

connection_string = "ssh -X -i 'Downloads/AWS4815.pem' ec2-user@" + IP

print "Connected using command: " + connection_string

sftp = ssh.open_sftp()

print "Uploading " + ( target_directory + ".zip" ) + " to " + (remote_directory + ".zip")
sftp.put(target_directory + ".zip", remote_directory + ".zip")
os.remove(target_directory + ".zip")

#sftp.put("/Users/Martin/Desktop/setup.sh", remote_prefix + "setup.sh")

def runInChannel(command):
    channel = ssh.get_transport().open_session()
    channel.exec_command(command)
    while True:
        if channel.exit_status_ready():
            break
        rl, wl, xl = select.select([channel], [], [], 0.0)
        if len(rl) > 0:
            print channel.recv(1024)

    channel.close();

#runInChannel("sh " + remote_prefix + "setup.sh");

print "Unzipping " + remote_directory;
runInChannel("unzip " + remote_directory)

print "Running HANDS..."

lib_reference = remote_directory + "/lib"

try:
    if (nokill == True):

        command = '/usr/bin/java -XX:+UseConcMarkSweepGC -XX:MaxDirectMemorySize=200g -XX:-UseGCOverheadLimit -Xmx200g -Dfile.encoding=US-ASCII -classpath ' + remote_director + '/bin:' + lib_reference + '/epsgraphics.jar:' + lib_reference + '/jcommon-1.0.21.jar:' + lib_reference + '/jfreechart-1.0.17.jar:' + lib_reference + '/jgrapht-core-0.9.0.jar:' + lib_reference + '/jgraph-sna.jar:' + lib_reference + '/java-plot.jar:' + lib_reference + '/commons-math3-3.4.1.jar:' + lib_reference + '/bsh-2.0b4.jar:' + lib_reference + '/mapdb.jar:' + lib_reference + '/fst-2.38-onejar.jar Utility.Runner -t';
        #stdin, stdout, stderr = client.exec_command(command)
        print command
    else:
        command = '/usr/bin/java -XX:+UseConcMarkSweepGC -XX:MaxDirectMemorySize=200g -XX:-UseGCOverheadLimit -Xmx200g -Dfile.encoding=US-ASCII -classpath ' + remote_directory + '/bin:' + lib_reference + '/epsgraphics.jar:' + lib_reference + '/jcommon-1.0.21.jar:' + lib_reference + '/jfreechart-1.0.17.jar:' + lib_reference + '/jgrapht-core-0.9.0.jar:' + lib_reference + '/jgraph-sna.jar:' + lib_reference + '/java-plot.jar:' + lib_reference + '/commons-math3-3.4.1.jar:' + lib_reference + '/bsh-2.0b4.jar:' + lib_reference + '/mapdb.jar:' + lib_reference + '/fst-2.38-onejar.jar Utility.Runner -t -k'
        #stdin, stdout, stderr = client.exec_command(command)
    
    print "cd " + remote_directory;
    
    print command

    #for line in stdout:
        #print '... ' + line.strip('\n')

except KeyboardInterrupt:
    print "Quitting HANDS."
    nocopy = True

sftp.close()
ssh.close()

#if (nocopy == False):
    #print "Simulation(s) complete. Moving files to main directory."

    #copymultiple(target_directory + "/output/data/", root_copy_directory + "/output/data/")

#if (nodelete == False):
    #print "Deleting temporary directory " + target_directory

    #shutil.rmtree(target_directory)