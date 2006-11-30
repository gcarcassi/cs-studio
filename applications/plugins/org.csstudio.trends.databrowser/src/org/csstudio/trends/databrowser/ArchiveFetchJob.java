/**
 * 
 */
package org.csstudio.trends.databrowser;

import org.csstudio.archive.ArchiveSamples;
import org.csstudio.archive.ArchiveServer;
import org.csstudio.archive.cache.ArchiveCache;
import org.csstudio.platform.model.IArchiveDataSource;
import org.csstudio.platform.util.ITimestamp;
import org.csstudio.trends.databrowser.model.IModelItem;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/** Eclipse background job for fetching samples from the data server.
 *  @author Kay Kasemir
 */
class ArchiveFetchJob extends Job
{
    private IModelItem item;
    private ITimestamp start, end;
    
    /** Create job that searches given server's keys for pattern,
     *  then notifies view about received names.
     */
    public ArchiveFetchJob(IModelItem item, ITimestamp start, ITimestamp end)
    {
        super(Messages.FetchDataForPV
                + "'" + item.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
        this.item = item;
        this.start = start;
        this.end = end;
        // TODO: Do we need to assert that only one data fetch runs at a time?
        // setRule()...?
    }

    /* @see org.eclipse.core.runtime.jobs.Job#run() */
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        IArchiveDataSource archives[] = item.getArchiveDataSources();
        monitor.beginTask(Messages.FetchingSample, archives.length);
        for (int i=0; i<archives.length; ++i)
        {
            // Display "N/total", using '1' for the first sub-archive.
            monitor.subTask(Messages.Fetch_Archive
                + "'" + archives[i].getName() //$NON-NLS-1$
                + "' (" //$NON-NLS-1$
                + (i+1) + "/" + archives.length + ")");  //$NON-NLS-1$//$NON-NLS-2$
            ArchiveCache cache = ArchiveCache.getInstance();
            try
            {   // Invoke the possibly lengthy search.
                ArchiveServer server = cache.getServer(archives[i].getUrl()); 
                int request_type = 
                    server.getRequestType(ArchiveServer.GET_PLOTBINNED);
                int request_parm = 800;
                ArchiveSamples result[] = server.getSamples(
                        archives[i].getKey(), new String[] { item.getName() },
                        start, end, request_type, request_parm);
                if (result.length == 1)
                {   // Notify model of new samples.
                    // Even when monitor.isCanceled at this point?
                    // Yes, since we have the samples, might as well show them
                    // before bailing out.
                    item.addArchiveSamples(result[0]);
                }
                else
                {
                    throw new Exception("Didn't get expected response"); //$NON-NLS-1$
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            // Stop and ignore further results when canceled.
            if (monitor.isCanceled())
                return Status.CANCEL_STATUS;
            // Handled one sub-archive.
            monitor.worked(1);
        }
        monitor.done();
        return Status.OK_STATUS;
    }
}