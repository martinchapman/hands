# rsync --progress -rave "ssh -i '<DownloadsPath>/AWS4815.pem'" <WorkspacePath>/SearchGames/output/temp/files.zip ec2-user@<INSERTIP>:/home/ec2-user/
# zip -r awscharts.zip output/charts 
# scp awscharts.zip martin@px205.dcs.kcl.ac.uk:<DownloadsPath>

## TEMPLATE FOR A LINUX INSTANCE THAT CAN RUN HANDS

echo "Installing TMUX"
sudo yum install tmux

# tmux

echo "Installing XAUTH"
sudo yum install xorg-x11-xauth

echo "Installing and changing JDK"
sudo yum install java-1.8.0-openjdk-devel
sudo alternatives --config java

java -version

## Homebrew

echo "Pre-Installing Homebrew"
PATH=/usr/bin:/bin
unset LD_LIBRARY_PATH PKG_CONFIG_PATH HOMEBREW_CC

prefix=~/.linuxbrew
PATH="$prefix/bin:$prefix/sbin:$PATH"

echo "Downloading Homebrew"
ruby -e "$(curl -fsSL https://raw.github.com/Homebrew/linuxbrew/go/install)"

#Press return

echo "Installing GCC"
sudo yum install gcc44 gcc44-c++
ln -s /usr/bin/gcc44 $prefix/bin/gcc-4.4
ln -s /usr/bin/g++44 $prefix/bin/g++-4.4
ln -s /usr/bin/gfortran44 $prefix/bin/gfortran-4.4
export HOMEBREW_CC=gcc-4.4

echo "Pre install programs"
sudo yum install git

brew tap homebrew/versions

echo "Running hello homebrew test"
brew install hello && brew test hello; brew remove hello

sudo yum install patch

brew install zlib

brew install expat

echo "Installing GNUPlot"
brew install gnuplot

sudo ln -s $(which gnuplot) /usr/bin/gnuplot

