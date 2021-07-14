from .interface import PyNar

nar = PyNar()

nar.debug(False)
print(nar.log_start())

nar.input('''
    <bird-->animal>.
    <robin-->bird>.
''')
nar.input('<robin-->animal>?')
cycles_left, outputs = nar.cycles(200)
nar.print_outputs(outputs)
print(f'cycles left: {cycles_left}')

nar.input('<penguin-->bird>.')
nar.input('<bird-->flyer>. %0.7%')
nar.input('<penguin-->flyer>?')
cycles_left, outputs = nar.cycles(200)
nar.print_outputs(outputs)
print(f'cycles left: {cycles_left}')

nar.input('<(*, {SELF})-->^left>!')
cycles_left, outputs = nar.cycles(10, True) # which is equivalent to `nar.input('10')`
print(f'cycles left: {cycles_left}')
nar.print_outputs()
nar.clear_outputs()
nar.print_outputs()

nar.log_stop()