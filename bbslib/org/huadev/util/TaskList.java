package org.huadev.util;

import java.util.Iterator;
import java.util.LinkedList;

public class TaskList implements Runnable{
	
	private LinkedList<Runnable> m_listTask;
	private int m_iProcessThreadCount;

	
	Runnable GetOneTask()
	{
		Runnable task = null;
		synchronized(m_listTask)
		{
			Iterator<Runnable> itTask = m_listTask.iterator();
			if(itTask.hasNext())
			{
				task = itTask.next();
				m_listTask.removeFirst();				
			}
			
			
			m_listTask.notify();			
		}
		
		System.out.println("not process task:"+m_listTask.size());
		
		return task;
	
		
	}
	
	class TaskProcesser implements Runnable
	{

		@Override
		public void run() {
				
			Runnable task = null;
			do
			{
				task = GetOneTask();
				if(task != null)
				{
					task.run();
				}
				else
				{
					break;
				}				
			}
			while(true);
			
		}
		
		
	}
	
	public TaskList(int iProcessThreadCount)
	{
		m_iProcessThreadCount = iProcessThreadCount;	
		if(m_iProcessThreadCount <= 0)
		{
			m_iProcessThreadCount = 1;
		}
		
		m_listTask = new LinkedList<Runnable>();
	}
	
	
	public void AddTask(Runnable task)
	{
		synchronized (m_listTask)
		{
			m_listTask.add(task);	
			m_listTask.notify();
		}
		
	}
	
	



	@Override
	public void run() {
		
		
		Thread[] processers = new Thread[m_iProcessThreadCount];
		
		for(int i = 0; i < m_iProcessThreadCount;i++)
		{
			TaskProcesser processerRunnable = new TaskProcesser();
	
			processers[i] =  new Thread(processerRunnable);
			processers[i].start(); 		
			
		}

		for(int i = 0; i < m_iProcessThreadCount;i++)
		{
			try {
				processers[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("task has finish");
	}
	
	 

}
