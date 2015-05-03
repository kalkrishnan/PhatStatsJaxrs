# -*- mode: ruby -*-
# vi: set ft=ruby :

# Specify Vagrant version and Vagrant API version
Vagrant.require_version ">= 1.6.0"
VAGRANTFILE_API_VERSION = "2"

# Create and configure the VM(s)
Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  # Assign a friendly name to this host VM
  config.vm.hostname = "docker-host"

  # Skip checking for an updated Vagrant box
  config.vm.box_check_update = false

  # Always use Vagrant's default insecure key
  config.ssh.insert_key = false

  # Spin up a "host box" for use with the Docker provider
  # and then provision it with Docker
  config.vm.box = "C:/Users/kalyank/Downloads/ubuntu-12.04-amd64-vbox.box"
  config.vm.synced_folder "C:/Users/kalyank/workspace/PhatStats", "/home/vagrant/PhatStats"
  # The url from where the 'config.vm.box' box will be fetched if it
  # doesn't already exist on the user's system.
   #config.vm.box_url = "https://dl.dropboxusercontent.com/s/uba887a4jas6qqz/ubuntu1110x64.box"
   config.vm.network :forwarded_port, guest: 8080, host: 8080
   config.vm.provision "shell", inline:
     "ls -LR"
  config.vm.provision "docker" do |d|
	 d.build_image "/home/vagrant/PhatStats", args: "-t 'phatstats'"
	 d.run "phatstats", args: " -p '8888:8080'"
  end
	#config.vm.provision "shell", inline:
    # "ps aux | grep 'sshd:' | awk '{print $2}' | xargs kill"
  # Disable synced folders (prevents an NFS error on "vagrant up")
  #config.vm.synced_folder ".", "/vagrant", disabled: true

end