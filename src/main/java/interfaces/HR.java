package interfaces;


public interface HR {
    void addWorker(final String teamName, final String workerName, final Class<?> serviceClass,
                   final Object... parameters);

    TeamBuilder createTeam(final String teamName, final String managerName, final Class<?> managerClass,
                           final Object... parameters);

    TeamBuilder createSubTeam(final String rootTeamName, final String subTeamManagerName, final Class<?> managerClass,
                              final Object... parameters);

    TeamState askTeam(final String teamName);

    void stopTeam(final String teamName);

    Monitor startProject();
}
