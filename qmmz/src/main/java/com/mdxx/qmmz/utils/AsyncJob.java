package com.mdxx.qmmz.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import android.os.Handler;
import android.os.Looper;

/*
 * 
 * 
 * 
 * AsyncJobLibrary
Android library to easily queue background and UI tasks

Index

Why AsyncJob?
So what does it exactly do?
How does it work?
Using static methods
Threading with ExecutorServices
How do I add it to my project?
Reference
Interfaces
AsyncJob static methods
AsyncJob object methods
AsyncJobBuilder methods
License
About me
Why AsyncJob?

In my latest projects I started using Android Annotations, which has an amazing set of tools to make creating Android applications a lot easier.

However, for some stuff I wanted to make having to inherit from auto-built classes was an impossible thing to do and I ended up having half of my project using AndroidAnnotations and the other half doing stuff manually.

So I thought: maybe I should try ButterKnife instead. That worked better, I could inject views whenever I wanted and I still had some of the "@Click" stuff I liked.

But not everything was good. AndroidAnnotations had a pair of annotations called @Background and @UIThread which magically -well, not so magically- allowed your code to be executed on the UI Thread or on a Background one with no effort. I REALLY missed those annotations as they allowed me to have a way cleaner code, so I started thinking how could I replace them.

AsyncJob was the closest I got to that.

So what does it exactly do?

If you are working on Android you probably have ended up using AsyncTasks to do background tasks and then have a response on the UI Thread. Well, I'll confess: I HATE ASYNCTASKS.

I don't see why I would need to extend a class EVERY FUCKING TIME I want to do some work on background. Also, having to create a Thread and a Handler EVERY FUCKING TIME I wanted to do some background work and have a response wasn't a good option.

So what I did was to create a library which does that for you.

How does it work?

It's really easy. If you want the library to work in a similar way to AsyncTask, you can create an AsyncJob<JobResult> where JobResult is the type or class of the item it will return.

But creating an AsyncJob was also a boring thing to do so I created an AsyncJobBuilder<T> which allows you to create AsyncJobs in a fast and clean way.

AsyncJobs have two interfaces which will be used to store your code and execute it on backgrund or on the main thread. These are AsyncAction<ActionResult> and AsyncResultAction<ActionResult>. They can be set by:

asyncJob.setActionInBackground(actionInBackground);
asyncJob.setActionOnResult(actionOnMainThread);
And when you use asyncJob.start() it will call those interfaces and execute your code.

Here you have an example of an AsyncJob created by an AsyncJobBuilder:

new AsyncJob.AsyncJobBuilder<Boolean>()
        .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
            @Override
            public Boolean doAsync() {
                // Do some background work
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        })
        .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                Toast.makeText(context, "Result was: " + result, Toast.LENGTH_SHORT).show();
        }
}).create().start();
Using static methods

Most of the time, though, I will prefer doing the following:

Execute some code in background.
Execute some code on the UI thread from that background thread whenever I have to, not just to return a value.
So how do yo do that?

You use the provided static methods, AsyncJob.doInBackground() and AsyncJob.doOnMainThread():

AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
    @Override
    public void doOnBackground() {

        // Pretend it's doing some background processing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create a fake result (MUST be final)
        final boolean result = true;

        // Send the result to the UI thread and show it on a Toast
        AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
            @Override
            public void doInUIThread() {
                Toast.makeText(context, "Result was: "+ result, Toast.LENGTH_SHORT).show();
            }
        });
    }
});
Which also have some interfaces made specially for them.

That's good, but I'd like to have a better control of my background threads!

Well, you can. You can provide an ExecutorService to an AsyncJob and the tasks that you want will be queued to it:

// Create a job to run on background
AsyncJob.OnBackgroundJob job = new AsyncJob.OnBackgroundJob() {
    @Override
    public void doOnBackground() {
        // Pretend to do some background processing
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // This toast should show a difference of 1000ms between calls
        AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
            @Override
            public void doInUIThread() {
                Toast.makeText(context, "Finished on: "+System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
            }
        });
    }
};

// This ExecutorService will run only a thread at a time
ExecutorService executorService = Executors.newSingleThreadExecutor();

// Send 5 jobs to queue which will be executed one at a time
for(int i = 0; i < 5; i++) {
    AsyncJob.doInBackground(job, executorService);
}
In this example, I am supplying a SingleThreadExecutor to the AsyncJob, which will only allow one thread to run at a time, serializing their execution. You can provide any other ExecutorServices tof it your needs.

How do I add it to my project?

Add it to your gradle dependencies like this:

dependencies {
    ...
    compile 'com.arasthel:asyncjob-library:1.0.3'
    ...
}

Also, you can manually download or clone this repo and import it to your current project as a Module.

Reference:

Interfaces:

// These are for AsyncJob objects
public interface AsyncAction<ActionResult> {
    public ActionResult doAsync();
}
public interface AsyncResultAction<ActionResult> {
    public void onResult(ActionResult result);
}

// These are for the static methods
public interface OnMainThreadJob {
    public void doInUIThread();
}
public interface OnBackgroundJob {
    public void doOnBackground();
}
AsyncJob static methods:

public static void doInBackground(OnBackgroundJob onBackgroundJob);


public static void doOnMainThread(final OnMainThreadJob onMainThreadJob);


public static FutureTask doInBackground(final OnBackgroundJob onBackgroundJob, ExecutorService executor);
AsyncJob object methods:

public AsyncJob<JobResult>();
// Sets the action to execute on background
public void setActionInBackground(AsyncAction actionInBackground);
public AsyncAction getActionInBackground();

// Sets an action to be executed when the background one ends and returns a result
public void setActionOnResult(AsyncResultAction actionOnMainThread);
public AsyncResultAction getActionOnResult();

// Sets the optional ExecutorService to queue the jobs
public void setExecutorService(ExecutorService executorService);
public ExecutorService getExecutorService();

// Cancels the AsyncJob interrupting the inner thread.
public void cancel();

// Starts the execution
public void start();
AsyncJobBuilder methods:

public AsyncJobBuilder<JobResult>();

public AsyncJobBuilder<JobResult> doInBackground(AsyncAction<JobResult> action);

public AsyncJobBuilder<JobResult> doWhenFinished(AsyncResultAction action);

public AsyncJobBuilder<JobResult> withExecutor(ExecutorService executor);

public AsyncJob<JobResult> create();
 * 
 */


/**
 * 
 * Created by Arasthel on 08/07/14.
 */
public class AsyncJob<JobResult> {

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    // Action to do in background
    private AsyncAction actionInBackground;
    // Action to do when the background action ends
    private AsyncResultAction actionOnMainThread;

    // An optional ExecutorService to enqueue the actions
    private ExecutorService executorService;

    // The thread created for the action
    private Thread asyncThread;
    // The FutureTask created for the action
    private FutureTask asyncFutureTask;

    // The result of the background action
    private JobResult result;

    /**
     * Instantiates a new AsyncJob
     */
    public AsyncJob() {
    }

    /**
     * Executes the provided code immediately on the UI Thread
     * @param onMainThreadJob Interface that wraps the code to execute
     */
    public static void doOnMainThread(final OnMainThreadJob onMainThreadJob) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                onMainThreadJob.doInUIThread();
            }
        });
    }

    /**
     * Executes the provided code immediately on a background thread
     * @param onBackgroundJob Interface that wraps the code to execute
     */
    public static void doInBackground(final OnBackgroundJob onBackgroundJob) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                onBackgroundJob.doOnBackground();
            }
        }).start();
    }

    /**
     * Executes the provided code immediately on a background thread that will be submitted to the
     * provided ExecutorService
     * @param onBackgroundJob Interface that wraps the code to execute
     * @param executor Will queue the provided code
     */
    public static FutureTask doInBackground(final OnBackgroundJob onBackgroundJob, ExecutorService executor) {
        FutureTask task = (FutureTask) executor.submit(new Runnable() {
            @Override
            public void run() {
                onBackgroundJob.doOnBackground();
            }
        });

        return task;
    }

    /**
     * Begins the background execution providing a result, similar to an AsyncTask.
     * It will execute it on a new Thread or using the provided ExecutorService
     */
    public void start() {
        if(actionInBackground != null) {

            Runnable jobToRun = new Runnable() {
                @Override
                public void run() {
                    result = (JobResult) actionInBackground.doAsync();
                    onResult();
                }
            };

            if(getExecutorService() != null) {
               asyncFutureTask = (FutureTask) getExecutorService().submit(jobToRun);
            } else {
                asyncThread = new Thread(jobToRun);
                asyncThread.start();
            }
        }
    }

    /**
     * Cancels the AsyncJob interrupting the inner thread.
     */
    public void cancel() {
        if(actionInBackground != null) {
            if(executorService != null) {
               asyncFutureTask.cancel(true);
            } else {
                asyncThread.interrupt();
            }
        }
    }


    private void onResult() {
        if (actionOnMainThread != null) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    actionOnMainThread.onResult(result);
                }
            });
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public AsyncAction getActionInBackground() {
        return actionInBackground;
    }

    /**
     * Specifies which action to run in background
     * @param actionInBackground the action
     */
    public void setActionInBackground(AsyncAction actionInBackground) {
        this.actionInBackground = actionInBackground;
    }

    public AsyncResultAction getActionOnResult() {
        return actionOnMainThread;
    }

    /**
     * Specifies which action to run when the background action is finished
     * @param actionOnMainThread the action
     */
    public void setActionOnResult(AsyncResultAction actionOnMainThread) {
        this.actionOnMainThread = actionOnMainThread;
    }

    public interface AsyncAction<ActionResult> {
        public ActionResult doAsync();
    }

    public interface AsyncResultAction<ActionResult> {
        public void onResult(ActionResult result);
    }

    public interface OnMainThreadJob {
        public void doInUIThread();
    }

    public interface OnBackgroundJob {
        public void doOnBackground();
    }

    /**
     * Builder class to instantiate an AsyncJob in a clean way
     * @param <JobResult> the type of the expected result
     */
    public static class AsyncJobBuilder<JobResult> {

        private AsyncAction<JobResult> asyncAction;
        private AsyncResultAction asyncResultAction;
        private ExecutorService executor;

        public AsyncJobBuilder() {

        }

        /**
         * Specifies which action to run on background
         * @param action the AsyncAction to run
         * @return the builder object
         */
        public AsyncJobBuilder<JobResult> doInBackground(AsyncAction<JobResult> action) {
            asyncAction = action;
            return this;
        }

        /**
         * Specifies which action to run when the background action ends
         * @param action the AsyncAction to run
         * @return the builder object
         */
        public AsyncJobBuilder<JobResult> doWhenFinished(AsyncResultAction action) {
            asyncResultAction = action;
            return this;
        }

        /**
         * Used to provide an ExecutorService to launch the AsyncActions
         * @param executor the ExecutorService which will queue the actions
         * @return the builder object
         */
        public AsyncJobBuilder<JobResult> withExecutor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }

        /**
         * Instantiates a new AsyncJob of the given type
         * @return a configured AsyncJob instance
         */
        public AsyncJob<JobResult> create() {
            AsyncJob<JobResult> asyncJob = new AsyncJob<JobResult>();
            asyncJob.setActionInBackground(asyncAction);
            asyncJob.setActionOnResult(asyncResultAction);
            asyncJob.setExecutorService(executor);
            return asyncJob;
        }

    }

}
