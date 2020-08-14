#AIDL (Android Interface Definition Language) is similar to other IDLs.

It allows us to define the programming interface that both the client 
and service agree upon in order to communicate with each other using interprocess communication (IPC).
On Android, one process cannot normally access the memory of another process. So to talk, 
they need to decompose their objects into primitives that the operating system can understand, 
and marshall the objects across that boundary for you. The code to do that marshaling is tedious to write, so Android handles it for you with AIDL.


# When to use AIDL

Clients from different applications want to access your service for IPC.
Your service wants to handle multithreading for IPC(any method defined in service can be executed simultaneously by more than one application).
If you want to share data and control something in another application.
You want to create some new functionalities and distribute them as a library.



1. Overall IPC 
- Inter-process communication (IPC) is a framework for the exchange of signals and data across multiple processes
- Used for message passing, synchronization, shared memory, and remote procedure calls (RPC)
- Enables information sharing, computational speedup, modularity, convenience, privilege separation, data isolation, stability

 +)Each process has its own (sandboxed) address space, typically running under a unique systemID

2. Types of IPC 

- Files (including memory mapped)
- Signals
- Sockets (UNIX domain, TCP/IP)
- Pipes (including named pipes)
- Semaphores
- Shared memory
- Message passing (including queues, message bus)
- Intents, ContentProviders, Messenger
- Binder!

3. IPC in android 
Android apps and system services run in separate processes for security, stability, and memory management reasons, but they need to communicate
  and share data!
- Security: each process is sandboxed and run under a distinct system identity
- Stability: if a process misbehaves (e.g. crashes), it does not affect any other processes
- Memory management: "unneeded" processes are removed to free resources (mainly memory) for new ones
- In fact, a single Android app can have its components run in separate processes
- Focused on scalability, stability, flexibility, low-latency/overhead, easy programming model

Most of IPC in android
Intents, ContentProviders, Messenger, Binder!

4. Intents and Content Providers
Intent messaging is a framework for asynchronous communication among Android components
- Those components may run in the same or across different apps (i.e. processes)
- Enables both point-to-point as well as publishsubscribe messaging domains
- The intent itself represents a message containing the description of the operation to be performed as well as data to be passed to the recipient(s)
- Implicit intents enable loosely-coupled APIs
ContentResolvers communicate synchronously with ContentProviders (typically running in separate apps) via a fixed (CRUD) API

But,
- Not well-suited for low-latency
- Since the APIs are loosely-defined, prone to run-time errors
- In fact, Intents and ContentProvider are just a higher-level abstraction of Binderr!

5. Messeger 
- Android’s Messenger represents a reference to a Handler that can be sent to a remote process via an Intent
- Messages sent by the remote process via the messenger are delivered to the local handler
- Messages are like Intents, in that they can designate the "operation" (aMessage.what) and data (aMessage.getData())
- Still asynchronous, but lower latency/overhead
- Efficient call-backs from the service to the client
- Messages are by default handled on the Looper thread

6. Binder 

7. What's Binder 
"Thread migration" - like programming model:
 - Automatic management of thread-pools
 - Methods on remote objects can be invoked as if they were local - the thread appears to "jump" to the other process
 - Synchronous and asynchronous (oneway) invocation model
Identifying senders to receivers (via UID/PID) - important for security reasons
Unique object-mapping across process boundaries
 - A reference to a remote object can be passed to yet another process and can be used as an identifying token
Ability to send file descriptors across process boundaries
Simple Android Interface Definition Language (AIDL)
Built-in support for marshalling many common data-types
Simplified transaction invocation model via auto-generated proxies and stubs (Java-only)
Local execution mode (no IPC/data marshalling) if the client and the service happen to be in the
same process

- Most app component life-cycle call-backs (e.g. onResume(), onDestory(), etc.) are invoked by
ActivityManagerService via binder

8. Why Binder?

9. Binder Terminology

10. Binder Communication and Discovery

11. AIDL

12. Binder object reference mapping across process boundaries

13. Demo 

- MusicPlayer App 
- SmartCharging App 

14. Rerference

https://developer.android.com/guide
https://medium.com/@budhdisharma/aidl-and-its-uses-in-android-e7a2520093e

15. Q&A
