# 1. Preparação dos jogadores

PreparadorDadosJogadores.carregar(dao) lê pelo DAO e confere 150 jogadores, 50 por perfil, IDs únicos e quatro características finitas.

ConjuntoDadosJogadores guarda a ordem da consulta. getMatrizOriginal() devolve uma cópia com horas, partidas, gasto e taxa de vitória. getIds() e getPerfis() usam for para manter a mesma ordem. ID e perfil não entram no treinamento.

A lista é protegida contra alteração. Jogador e DAO existentes não foram modificados.
