     Create Cached Table Subnet (
        Obj OBJECT, 
        Id INTEGER IDENTITY PRIMARY KEY, 
        Name VARCHAR(1024) NOT NULL, 
        UNIQUE (Name)
        );
      GO
     Create Cached Table Node (
        Obj OBJECT, 
        Id INTEGER IDENTITY PRIMARY KEY, 
        Name VARCHAR(1024) NOT NULL, 
        UNIQUE (Name)
        );
      GO
     Create Cached Table Port (
        Obj OBJECT, 
        Id INTEGER IDENTITY PRIMARY KEY, 
        Name VARCHAR(1024) NOT NULL, 
        UNIQUE (Name)
        );
      GO
     Create Cached Table Topology (
        Obj OBJECT, 
        Id INTEGER IDENTITY PRIMARY KEY, 
        Name VARCHAR(1024) NOT NULL, 
        UNIQUE (Name)
        );
      GO      