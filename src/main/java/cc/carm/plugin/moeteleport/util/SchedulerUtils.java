

package cc.carm.plugin.moeteleport.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SchedulerUtils {
    private final JavaPlugin plugin;

    public SchedulerUtils(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private JavaPlugin getPlugin() {
        return this.plugin;
    }

    public void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(this.getPlugin(), runnable);
    }

    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.getPlugin(), runnable);
    }

    public void runLater(long delay, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(this.getPlugin(), runnable, delay);
    }

    public void runLaterAsync(long delay, Runnable runnable) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.getPlugin(), runnable, delay);
    }

    public void runAtInterval(long interval, Runnable... tasks) {
        this.runAtInterval(0L, interval, tasks);
    }

    public void runAtInterval(long delay, long interval, Runnable... tasks) {
        (new BukkitRunnable() {
            private int index;

            public void run() {
                if (this.index >= tasks.length) {
                    this.cancel();
                } else {
                    tasks[this.index].run();
                    ++this.index;
                }
            }
        }).runTaskTimer(this.getPlugin(), delay, interval);
    }

    public void runAtIntervalAsync(long interval, Runnable... tasks) {
        this.runAtIntervalAsync(0L, interval, tasks);
    }

    public void runAtIntervalAsync(long delay, long interval, Runnable... tasks) {
        (new BukkitRunnable() {
            private int index;

            public void run() {
                if (this.index >= tasks.length) {
                    this.cancel();
                } else {
                    tasks[this.index].run();
                    ++this.index;
                }
            }
        }).runTaskTimerAsynchronously(this.getPlugin(), delay, interval);
    }

    public void repeat(int repetitions, long interval, Runnable task, Runnable onComplete) {
        (new BukkitRunnable() {
            private int index;

            public void run() {
                ++this.index;
                if (this.index >= repetitions) {
                    this.cancel();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                } else {
                    task.run();
                }
            }
        }).runTaskTimer(this.getPlugin(), 0L, interval);
    }

    public void repeatAsync(int repetitions, long interval, Runnable task, Runnable onComplete) {
        (new BukkitRunnable() {
            private int index;

            public void run() {
                ++this.index;
                if (this.index >= repetitions) {
                    this.cancel();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                } else {
                    task.run();
                }
            }
        }).runTaskTimerAsynchronously(this.getPlugin(), 0L, interval);
    }

    public void repeatWhile(long interval, Callable<Boolean> predicate, Runnable task, Runnable onComplete) {
        (new BukkitRunnable() {
            public void run() {
                try {
                    if (!(Boolean)predicate.call()) {
                        this.cancel();
                        if (onComplete == null) {
                            return;
                        }

                        onComplete.run();
                        return;
                    }

                    task.run();
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        }).runTaskTimer(this.getPlugin(), 0L, interval);
    }

    public void repeatWhileAsync(long interval, Callable<Boolean> predicate, Runnable task, Runnable onComplete) {
        (new BukkitRunnable() {
            public void run() {
                try {
                    if (!(Boolean)predicate.call()) {
                        this.cancel();
                        if (onComplete == null) {
                            return;
                        }

                        onComplete.run();
                        return;
                    }

                    task.run();
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        }).runTaskTimerAsynchronously(this.getPlugin(), 0L, interval);
    }

    public class TaskBuilder {
        private final Queue<SchedulerUtils.Task> taskList = new LinkedList<>();

        public TaskBuilder() {
        }

        public SchedulerUtils.TaskBuilder append(SchedulerUtils.TaskBuilder builder) {
            this.taskList.addAll(builder.taskList);
            return this;
        }

        public SchedulerUtils.TaskBuilder appendDelay(long delay) {
            this.taskList.add((onComplete) -> SchedulerUtils.this.runLater(delay, onComplete));
            return this;
        }

        public SchedulerUtils.TaskBuilder appendTask(Runnable task) {
            this.taskList.add((onComplete) -> {
                task.run();
                onComplete.run();
            });
            return this;
        }

        public SchedulerUtils.TaskBuilder appendTask(SchedulerUtils.Task task) {
            this.taskList.add(task);
            return this;
        }

        public SchedulerUtils.TaskBuilder appendDelayedTask(long delay, Runnable task) {
            this.taskList.add((onComplete) -> SchedulerUtils.this.runLater(delay, () -> {
                task.run();
                onComplete.run();
            }));
            return this;
        }

        public SchedulerUtils.TaskBuilder appendTasks(long delay, long interval, Runnable... tasks) {
            this.taskList.add((onComplete) -> {
                Runnable[] all = Arrays.copyOf(tasks, tasks.length + 1);
                all[all.length - 1] = onComplete;
                SchedulerUtils.this.runAtInterval(delay, interval, all);
            });
            return this;
        }

        public SchedulerUtils.TaskBuilder appendRepeatingTask(int repetitions, long interval, Runnable task) {
            this.taskList.add((onComplete) -> SchedulerUtils.this.repeat(repetitions, interval, task, onComplete));
            return this;
        }

        public SchedulerUtils.TaskBuilder appendConditionalRepeatingTask(long interval, Callable<Boolean> predicate, Runnable task) {
            this.taskList.add((onComplete) -> SchedulerUtils.this.repeatWhile(interval, predicate, task, onComplete));
            return this;
        }

        public SchedulerUtils.TaskBuilder waitFor(Callable<Boolean> predicate) {
            this.taskList.add((onComplete) -> (new BukkitRunnable() {
                public void run() {
                    try {
                        if (!(Boolean)predicate.call()) {
                            return;
                        }

                        this.cancel();
                        onComplete.run();
                    } catch (Exception var2) {
                        var2.printStackTrace();
                    }

                }
            }).runTaskTimer(SchedulerUtils.this.getPlugin(), 0L, 1L));
            return this;
        }

        public void runTasks() {
            this.startNext();
        }

        private void startNext() {
            SchedulerUtils.Task task = this.taskList.poll();
            if (task != null) {
                task.start(this::startNext);
            }
        }
    }

    public interface Task {
        void start(Runnable onComplete);
    }
}
