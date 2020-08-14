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