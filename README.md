# PyNar

Python interface to NARS (Java).

## QuickStart

Import and instantiate:

``` Python
from pynar import PyNar
nar = PyNar()
```

Input multiple lines:

``` Python
nar.input('''
    <penguin-->bird>.
    <bird-->flyer>. %0.7%
''')
```

Input one line:

```Python
nar.input('<penguin-->flyer>?')
```

Run the reasoner with a maximun cycles:

```Python
cycles_left, outputs = nar.cycles(200)
```

If there is any objects in the output queue, the reasoning loop will be breaked, and the cycles left is available for developers.

Print the outputs:
```Python
nar.print_outputs(outputs)
print(f'cycles left: {cycles_left}')
```

Force the reasoner to run a fix #cycles without the break:

```Python
nar.input('<(*, {SELF})-->^left>!')
cycles_left, outputs = nar.cycles(10, force=True) # which is equivalent to `nar.input('10')`
print(f'cycles left: {cycles_left}')
nar.print_outputs(outputs)
```

After the forced cycles, we should access the outputs and clear them explicitly:

```Python
outputs = nar.get_outputs(clear=False) # default: clear=True
nar.print(outputs)
nar.clear_outputs()
nar.print(outputs)
```

Start and stop logging:

```Python
nar.log_start(append=True, log_path: str='./log_example.txt') # default: append=False, log_path: str='./log.txt'
nar.log_stop()
```

Note that the `log_start` should be called at first; `log_stop` should be called finally, otherwise the logging content would not be saved.

Enable or disable printing debugging information:

```Python
nar.debug() # or nar.debug(True) to enable.
nar.debug(False) # to disable.
```


## Installation

### Dependencies

Before install PyNar, some dependecies should be met first, including:

1. Java Environment.
2. OpenNARS project.

The interface is dependent on [OpenNARS v3.0.4][1]. Please make sure that OpenNARS project has been built, and `*.class` or `*.jar` files are available.

For example, after cloning the OpenNARS project to `D:/Codes/opennars` and build them in [IntelliJ IDEA](https://www.jetbrains.com/idea/download/), the target `*.class` files are generated in `<opennars_dir>\opennars\target\classes`, e.g. 

```
D:\Codes\opennars\opennars\target\classes
```

We will refer to this path in [Installation](#installation).

To build [OpenNARS][3], please refer to [README.md][2] of OpenNARS. Roughly, we shoud:

1. Configure Java Environment.
2. Install IntelliJ IDEA.
3. Build the OpenNARS project.

We might not follow the steps if we have installed them or we have had the target `*.class` or `*.jar` files of OpenNARS. Note that the Java packages `com.google.common.io.Resources` and `org/apache/commons/lang3/StringUtils` are needed, which could be automatically installed if we build the project with IntelliJ IDEA; if so, we should find the corresponding paths of the two packages, for example, they are

```
C:\Users\<Username>\.m2\repository\com\google\guava\guava\25.1-jre\guava-25.1-jre.jar
C:\Users\<Username>\.m2\repository\org\apache\commons\commons-lang3\3.7\commons-lang3-3.7.jar
```

Of course, we may also downlowd the packages and store them in other directories.


### Installing PyNar

Now Java Environment is configured and the OpenNARS project has been built.

In order to use the python interface to NARS, we should

1. copy the file PyNar.java into the directory `<opennars_dir>\opennars\src\main\java\org\opennars\main`, and rebuild the project,
2. edit the file `paths.txt`, adding into it the paths
   -  to the directory of `.class` files, i.e. `<opennars_dir>\\opennars\\target\\classes`,
   - to the package `guava-25.1-jre.jar`, e.g. `C:\\Users\\<Username>\\.m2\\repository\\com\\google\\guava\\guava\\25.1-jre\\guava-25.1-jre.jar`,
   - and to the package `commons-lang3-3.7.jar`, e.g. `C:\\Users\\<Username>\\.m2\\repository\\org\\apache\\commons\\commons-lang3\\3.7\\commons-lang3-3.7.jar`.
3. run cmd `python -m pynar.path_config <dir>\paths.txt` to copy the `paths.txt` into the root directory of the module `PyNar`.
4. run the demo
   ```
   python -m pynar.demo
   ```

If we see the outputs as follows, then the installation is finished successfully.

```
True
[Answer] $0.2111;0.9000;1.0000$ <robin --> animal>?  :: <robin --> animal>. %1.00;0.81% 
cycles left: 200
[Answer] $0.4653;0.9000;1.0000$ <penguin --> flyer>?  :: <penguin --> flyer>. %0.70;0.57% 
cycles left: 62
cycles left: 0
[EXE] $0.40;0.90;0.95$ ^left([{SELF}])=null
```

## Links

 - OpenNARS: https://github.com/opennars/opennars
 - OpenNARS v3.0.4 (Stable Release Version): https://github.com/opennars/opennars/releases/tag/v3.0.4

[1]: https://github.com/opennars/opennars/releases/tag/v3.0.4
[2]: https://github.com/opennars/opennars/blob/master/README.md
[3]: https://github.com/opennars/opennars