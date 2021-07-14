from pynar import PyNar

nar = PyNar()

nar.debug(True)
print(nar.log_start())

nar.input('''
    <penguin-->bird>.
    <bird-->flyer>. %0.7%
''')
nar.input('<penguin-->flyer>?')
cycles_left, outputs = nar.cycles(200)
nar.print_outputs(outputs)
print(f'cycles left: {cycles_left}')

nar.input('<(*, {SELF})-->^left>!')
cycles_left, outputs = nar.cycles(10, True) # which is equivalent to `nar.input('10')`
print(f'cycles left: {cycles_left}')
nar.print_outputs(outputs)

outputs = nar.get_outputs(clear=False)
nar.print(outputs)
nar.clear_outputs()
nar.print(outputs)

nar.log_stop()