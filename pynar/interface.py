from pathlib import Path
file_paths = Path(__file__).parent.absolute()/'paths.txt'
with open(file_paths, "r") as f: 
    paths = f.read().splitlines() 
    # print(paths)

import jnius_config
jnius_config.add_classpath(*paths)

from jnius import autoclass

class PyNar:
    def __init__(self) -> None:
        self.nar = autoclass('org.opennars.main.PyNar')()

    def debug(self, d: bool=True):
        self.nar.debug(d)

    def _set_log_path(self, log_path):
        self.nar.set_log_path(str(log_path))
    
    def log_start(self, append: bool=False, log_path: str='./log.txt'):
        self._set_log_path(log_path)
        return self.nar.log_start(append)

    def log_stop(self):
        self.nar.log_stop()
    
    def input(self, text):
        self.nar.addInput(text)

    def get_outputs(self, clear: bool=True):
        queue = self.nar.get_queue()
        if clear: self.clear_outputs()
        return queue
    
    def clear_outputs(self):
        return self.nar.clear_queue()

    def print_outputs(self, queue=None, clear: bool=False):
        if queue is None:
            queue = self.get_outputs(clear)
        for q in queue:
            type_q = q[0].toString().split('$')[-1]
            if type_q == 'Answer':
                value_q = q[1][0].toString() + ' :: ' + q[1][1].toString()
            elif type_q == 'EXE':
                value_q = q[1].toString()
            print('\033[0;32m[' + type_q + ']\033[0m ' + value_q)
        

    def cycles(self, max_cycs: int, force: bool=False):
        '''
        Args:
            max_cycs (int): maximum cycles to execute.
            force (bool): if `force == True`, then once some outputs (e.g. `EXE` or `Answer`s) occur the reasoning loop would be breaked, and cycles left and outputs queue are returned. If `force == False`, then no matter how many outputs occurs, the reasoning step are forced to excute by `max_cycs` times without a break.
        Returns:
            cycles_left (int): cycles left to be excuted.
            outputs (list): outputs from NARS.
        '''
        if not force:
            results = self.nar.cycles(max_cycs)
            cycles_left = results['cycles_left']
            outputs = results['queue_out']
        else:
            self.input(str(max_cycs))
            cycles_left = 0
            outputs = self.get_outputs(False)
        return cycles_left, outputs




