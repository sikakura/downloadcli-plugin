package net.bowbow.jenkins;

import hudson.Extension;
import hudson.FilePath;
import hudson.cli.CLICommand;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.Item;

import org.kohsuke.args4j.Argument;

@Extension
public class DownloadCommand extends CLICommand {

    @Override
    public String getShortDescription() {
        return "Download a file.";
    }

    @Argument(metaVar = "JOB", usage = "Name of the job that contain of download file.", index = 0, required = true)
    public AbstractProject<?, ?> job;

    @Argument(metaVar = "FILE", usage = "Name of a file.", index = 1, required = true)
    public String filename;

    @Override
    protected int run() throws Exception {
        Hudson h = Hudson.getInstance();
        h.checkPermission(Item.BUILD);
        FilePath src = null;
        try {
            src = new FilePath(job.getSomeWorkspace(), filename);
            if (!src.exists()) {
                stderr.println("filename : " + filename + " is not exist.");
                return -1;
            }
            new FilePath(channel, filename).copyFrom(src);
        } catch (Exception e) {
            stderr.println(e.getMessage());
            return -1;
        }
        return 0;
    }
}
